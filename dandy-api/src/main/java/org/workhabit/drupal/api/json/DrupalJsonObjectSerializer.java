package org.workhabit.drupal.api.json;

import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.workhabit.drupal.api.entity.drupal7.DrupalNode;
import org.workhabit.drupal.api.entity.drupal7.DrupalField;
import org.workhabit.drupal.api.entity.drupal7.DrupalTaxonomyTerm;
import org.workhabit.drupal.api.site.exceptions.DrupalFetchException;

import java.util.*;

/**
 * Copyright 2009 - WorkHabit, Inc. - acs
 * Date: Oct 22, 2010, 10:51:49 AM
 */
public class DrupalJsonObjectSerializer<T> {
    private final Class<T> clazz;
    private final Gson gson;

    DrupalJsonObjectSerializer(Class<T> clazz) {
        this.clazz = clazz;
        GsonBuilder builder = new GsonBuilder();
        UnixTimeDateAdapter dateAdapter = new UnixTimeDateAdapter();
        builder.registerTypeAdapter(Date.class, dateAdapter);
        BooleanAdapter booleanAdapter = new BooleanAdapter();
        builder.registerTypeAdapter(Boolean.class, booleanAdapter);
        builder.setExclusionStrategies(new ExclusionStrategy() {

            public boolean shouldSkipField(FieldAttributes f) {
                return "taxonomy".equals(f.getName())
                       || "fields".equals(f.getName());
            }

            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gson = builder.create();
    }

    public String serialize(T object) throws JSONException {
        String json = gson.toJson(object);
        if (object.getClass() == DrupalNode.class) {
            DrupalNode n = (DrupalNode) object;
            Map<String, DrupalField> fieldMap = n.getFields();
            if (fieldMap != null && fieldMap.size() > 0) {
                // process any fields
                //
                JSONObject jsonObject = new JSONObject(json);
                for (Map.Entry<String, DrupalField> drupalFields : fieldMap.entrySet()) {
                    String name = drupalFields.getKey();
                    JSONObject jsonField = new JSONObject();
                    ArrayList<HashMap<String, String>> values = drupalFields.getValue().getValues();
                    for (int i = 0; i < values.size(); i++) {
                        JSONObject jsonValue = new JSONObject();
                        HashMap<String, String> value = values.get(i);
                        for (Map.Entry<String, String> entry : value.entrySet()) {
                            if (entry.getValue().startsWith("{")) {
                                // it's an object, try to deserialize
                                //
                                jsonValue.put(entry.getKey(), new JSONObject(entry.getValue()));
                            } else {
                                jsonValue.put(entry.getKey(), entry.getValue());
                            }
                        }
                        jsonField.put(String.valueOf(i), jsonValue);
                    }
                    jsonObject.put(name, jsonField);
                }
                json = jsonObject.toString();
            }
        }
        return json;
    }

    public T unserialize(String json) throws DrupalFetchException, JSONException {
        JSONObject dataObject = new JSONObject(json);
        T t = gson.fromJson(json, clazz);
        if (clazz == DrupalNode.class) {
            // special handling for cck fields.
            Iterator keys = dataObject.keys();
            Map<String, DrupalField> fields = new HashMap<String, DrupalField>();
            HashMap<Integer, DrupalTaxonomyTerm> terms = new HashMap<Integer, DrupalTaxonomyTerm>();
            while (keys.hasNext()) {
                String name = (String) keys.next();
                if (name.startsWith("field_")) {
                    // process cck field
                    //
                    DrupalField field = new DrupalField();
                    ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();
                    JSONArray cckFieldArray = dataObject.getJSONArray(name);
                    for (int i = 0; i < cckFieldArray.length(); i++) {
                        if (cckFieldArray.isNull(i)) {
                            continue;
                        }
                        JSONObject o = cckFieldArray.getJSONObject(i);
                        field.setName(name);
                        Iterator objectKeys = o.keys();
                        HashMap<String, String> valueMap = new HashMap<String, String>();
                        while (objectKeys.hasNext()) {
                            String next = (String) objectKeys.next();
                            valueMap.put(next, o.getString(next));
                        }
                        values.add(valueMap);
                    }
                    field.setValues(values);
                    fields.put(field.getName(), field);
                } else if ("taxonomy".equals(name)) {
                    // handle serialization of Taxonomy differently than normal maps
                    Object taxonomy = dataObject.get("taxonomy");
                    if (taxonomy instanceof JSONArray) {
                        // empty
                    } else {
                        JSONObject taxonomyObject = (JSONObject) taxonomy;
                        Iterator iterator = taxonomyObject.keys();
                        DrupalJsonObjectSerializer<DrupalTaxonomyTerm> taxonomyTermDrupalJsonObjectSerializer = DrupalJsonObjectSerializerFactory.getInstance(DrupalTaxonomyTerm.class);
                        while (iterator.hasNext()) {
                            String next = (String) iterator.next();
                            JSONObject taxonomyTerm = taxonomyObject.getJSONObject(next);
                            DrupalTaxonomyTerm term = taxonomyTermDrupalJsonObjectSerializer.unserialize(taxonomyTerm.toString());
                            terms.put(term.getTid(), term);
                        }

                    }
                }

            }
            DrupalNode n = (DrupalNode) t;
            n.setFields(fields);
            n.setTaxonomy(terms);
        }
        return t;
    }

    private JSONObject extractDataObject(String json) throws JSONException, DrupalFetchException {
        JSONObject objectResult = new JSONObject(json);
        assertNoErrors(objectResult);
        if (objectResult.has("#data")) {
            return objectResult.getJSONObject("#data");
        }
        return objectResult;
    }

    public List<T> unserializeList(String json) throws DrupalFetchException, JSONException {
        JSONArray jsonArray = extractDataArray(json);
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(unserialize(jsonArray.getJSONObject(i).toString()));
        }
        return list;
    }

    private JSONArray extractDataArray(String json) throws JSONException {
        return new JSONArray(json);
    }

    private void assertNoErrors(JSONObject objectResult) throws JSONException, DrupalFetchException {
        if (objectResult.has("#error") && objectResult.getBoolean("#error")) {
            throw new DrupalFetchException(objectResult);
        }
    }
}
