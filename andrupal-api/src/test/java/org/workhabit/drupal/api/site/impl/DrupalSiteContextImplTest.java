package org.workhabit.drupal.api.site.impl;

import com.google.gson.reflect.TypeToken;
import org.hamcrest.collection.IsMapContaining;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.workhabit.drupal.api.entity.DrupalNode;
import org.workhabit.drupal.api.entity.DrupalUser;
import org.workhabit.drupal.api.site.DrupalFetchException;
import org.workhabit.drupal.http.JsonRequestManager;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Copyright 2009 - WorkHabit, Inc. - acs
 * Date: Oct 25, 2010, 1:43:22 PM
 */
public class DrupalSiteContextImplTest {
    private DrupalSiteContextImpl drupalSiteContext;
    private JsonRequestManager mockJsonRequestManager;
    private Mockery context;
    private String siteUrl;

    @Before
    public void setUp() throws Exception {
        siteUrl = "http://ad.hourglassone.com";
        drupalSiteContext = new DrupalSiteContextImpl(siteUrl);
        context = new Mockery();
        mockJsonRequestManager = context.mock(JsonRequestManager.class);
        drupalSiteContext.setJsonRequestManager(mockJsonRequestManager);
    }

    @Test
    public void testConnect() throws Exception {
        setConnectExpectations();
        drupalSiteContext.connect();
    }

    @Test
    public void testConnectFailed() throws Exception {
        setConnectFailedExpectations();
        try {
            drupalSiteContext.connect();
            fail("Should have thrown exception");
        } catch (DrupalFetchException e) {
            // OK
        }
    }

    private void setConnectExpectations() throws IOException {
        context.checking(new Expectations() {
            {
                Class<? super Map<String, Object>> type = new TypeToken<Map<String, Object>>() {
                }.getRawType();
                //noinspection unchecked
                one(mockJsonRequestManager).post(with(equal(siteUrl + "/services/json")), with(equal("system.connect")), (Map<String, Object>) with(aNull(type)), with(equals(false)));
                String json = "{\"#error\":false,\"#data\":\"\"}";
                will(returnValue(json));
            }
        });
    }

    private void setConnectFailedExpectations() throws IOException {
        context.checking(new Expectations() {
            {
                Class<? super Map<String, Object>> type = new TypeToken<Map<String, Object>>() {
                }.getRawType();
                //noinspection unchecked
                one(mockJsonRequestManager).post(with(equal(siteUrl + "/services/json")), with(equal("system.connect")), (Map<String, Object>) with(aNull(type)), with(equals(false)));
                String json = "{\"#error\":true,\"#data\":\"Error Message\"}";
                will(returnValue(json));
            }
        });
    }

    @Test
    public void testLogout() throws Exception {
        context.checking(new Expectations() {
            {
                Class<? super Map<String, Object>> type = new TypeToken<Map<String, Object>>() {
                }.getRawType();
                //noinspection unchecked
                one(mockJsonRequestManager).postSigned(with(equal(siteUrl + "/services/json")), with(equal("user.logout")), (Map<String, Object>) with(aNull(type)), with(equals(false)));
                String json = "{\"#error\":false,\"#data\":\"\"}";
                will(returnValue(json));
                //noinspection unchecked
                one(mockJsonRequestManager).post(with(equal(siteUrl + "/services/json")), with(equal("system.connect")), (Map<String, Object>) with(aNull(type)), with(equals(false)));
                json = "{\"#error\":false,\"#data\":\"\"}";
                will(returnValue(json));
            }
        });
        drupalSiteContext.logout();
    }

    @Test
    public void testGetNodeView() throws Exception {
        /*CommonsHttpClientJsonRequestManager manager = new CommonsHttpClientJsonRequestManager();
        KeyRequestSigningInterceptorImpl requestSigningInterceptor = new KeyRequestSigningInterceptorImpl();
        requestSigningInterceptor.setDrupalDomain("workhabit.com");
        requestSigningInterceptor.setPrivateKey("9e47c52fae3c36baff404f7072e46547");
        manager.setRequestSigningInterceptor(requestSigningInterceptor);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("view_name", "andrupal_recent");
        String json = manager.post("http://ad.hourglassone.com/services/json", "views.get", data);
        System.out.println(json);
                                      */
        context.checking(new Expectations() {
            {
                Class<? super Map<String, Object>> type = new TypeToken<Map<String, Object>>() {
                }.getRawType();
                //noinspection unchecked
                one(mockJsonRequestManager).post(with(equal(siteUrl + "/services/json")), with(equal("system.connect")), (Map<String, Object>) with(aNull(type)), with(equals(false)));
                String json = "{\"#error\":false,\"#data\":\"\"}";
                will(returnValue(json));

                one(mockJsonRequestManager).postSigned(
                        with(equal(siteUrl + "/services/json")),
                        with(equal("views.get")),
                        with(IsMapContaining.hasEntry("view_name", (Object) "andrupal_recent")),
                        with(equals(false))
                );
                json = "{\"#error\":false,\"#data\":[{\"nid\":\"3\",\"type\":\"page\",\"language\":\"\",\"uid\":\"1\",\"status\":\"1\",\"created\":\"1285472651\",\"changed\":\"1287462859\",\"comment\":\"0\",\"promote\":\"0\",\"moderate\":\"0\",\"sticky\":\"0\",\"tnid\":\"0\",\"translate\":\"0\",\"vid\":\"3\",\"revision_uid\":\"1\",\"title\":\"More Content\",\"body\":\"\\u003ca href=\\\"http:\\/\\/google.com\\/\\\"\\u003eVisit Google\\u003c\\/a\\u003e\\r\\n\\r\\nAssertively empower robust resources with multimedia based ROI. Efficiently initiate mission-critical testing procedures without cross-platform e-tailers. Energistically engineer emerging \\\"outside the box\\\" thinking before fully tested process improvements. \\r\\n\\r\\nUniquely provide access to timely collaboration and idea-sharing without backward-compatible schemas. Dynamically integrate compelling bandwidth vis-a-vis compelling results. Competently generate one-to-one ROI after global products.\\r\\nAssertively empower robust resources with multimedia based ROI. Efficiently initiate mission-critical testing procedures without cross-platform e-tailers. Energistically engineer emerging \\\"outside the box\\\" thinking before fully tested process improvements. \\r\\n\\r\\nUniquely provide access to timely collaboration and idea-sharing without backward-compatible schemas. Dynamically integrate compelling bandwidth vis-a-vis compelling results. Competently generate one-to-one ROI after global products.\\r\\nAssertively empower robust resources with multimedia based ROI. Efficiently initiate mission-critical testing procedures without cross-platform e-tailers. Energistically engineer emerging \\\"outside the box\\\" thinking before fully tested process improvements. \\r\\n\\r\\nUniquely provide access to timely collaboration and idea-sharing without backward-compatible schemas. Dynamically integrate compelling bandwidth vis-a-vis compelling results. Competently generate one-to-one ROI after global products.Assertively empower robust resources with multimedia based ROI. Efficiently initiate mission-critical testing procedures without cross-platform e-tailers. Energistically engineer emerging \\\"outside the box\\\" thinking before fully tested process improvements. \\r\\n\\r\\nUniquely provide access to timely collaboration and idea-sharing without backward-compatible schemas. Dynamically integrate compelling bandwidth vis-a-vis compelling results. Competently generate one-to-one ROI after global products.\",\"teaser\":\"\\u003ca href=\\\"http:\\/\\/google.com\\/\\\"\\u003eVisit Google\\u003c\\/a\\u003e\\r\\n\\r\\nAssertively empower robust resources with multimedia based ROI. Efficiently initiate mission-critical testing procedures without cross-platform e-tailers. Energistically engineer emerging \\\"outside the box\\\" thinking before fully tested process improvements. \\r\\n\\r\\nUniquely provide access to timely collaboration and idea-sharing without backward-compatible schemas. Dynamically integrate compelling bandwidth vis-a-vis compelling results. Competently generate one-to-one ROI after global products.\\r\",\"log\":\"\",\"revision_timestamp\":\"1287462859\",\"format\":\"1\",\"name\":\"admin\",\"picture\":\"\",\"data\":\"a:0:{}\",\"last_comment_timestamp\":\"1285472651\",\"last_comment_name\":null,\"comment_count\":\"0\",\"taxonomy\":{\"1\":{\"tid\":\"1\",\"vid\":\"1\",\"name\":\"Category 1\",\"description\":\"\",\"weight\":\"0\"}}},{\"nid\":\"2\",\"type\":\"page\",\"language\":\"\",\"uid\":\"1\",\"status\":\"1\",\"created\":\"1285468757\",\"changed\":\"1287462868\",\"comment\":\"0\",\"promote\":\"0\",\"moderate\":\"0\",\"sticky\":\"0\",\"tnid\":\"0\",\"translate\":\"0\",\"vid\":\"2\",\"revision_uid\":\"1\",\"title\":\"Another display\",\"body\":\"Giving this a try\",\"teaser\":\"Giving this a try\",\"log\":\"\",\"revision_timestamp\":\"1287462868\",\"format\":\"1\",\"name\":\"admin\",\"picture\":\"\",\"data\":\"a:0:{}\",\"last_comment_timestamp\":\"1285468757\",\"last_comment_name\":null,\"comment_count\":\"0\",\"taxonomy\":{\"1\":{\"tid\":\"1\",\"vid\":\"1\",\"name\":\"Category 1\",\"description\":\"\",\"weight\":\"0\"}}},{\"nid\":\"1\",\"type\":\"story\",\"language\":\"\",\"uid\":\"1\",\"status\":\"1\",\"created\":\"1285374480\",\"changed\":\"1287462879\",\"comment\":\"2\",\"promote\":\"1\",\"moderate\":\"0\",\"sticky\":\"0\",\"tnid\":\"0\",\"translate\":\"0\",\"vid\":\"1\",\"revision_uid\":\"1\",\"title\":\"Test Title\",\"body\":\"Test body. Adding some more text here.\\r\\n\\r\\nIntrinsicly provide access to process-centric experiences via business benefits. Synergistically network interoperable internal or \\\"organic\\\" sources for standards compliant experiences. Credibly predominate 24\\/365 products for superior initiatives.\\r\\n\\r\\nDynamically optimize 24\\/7 leadership skills rather than distinctive web-readiness. Proactively parallel task intuitive platforms before timely action items. Quickly enhance exceptional schemas before highly efficient sources. \\r\\n\",\"teaser\":\"Test body. Adding some more text here.\\r\\n\\r\\nIntrinsicly provide access to process-centric experiences via business benefits. Synergistically network interoperable internal or \\\"organic\\\" sources for standards compliant experiences. Credibly predominate 24\\/365 products for superior initiatives.\\r\\n\\r\\nDynamically optimize 24\\/7 leadership skills rather than distinctive web-readiness. Proactively parallel task intuitive platforms before timely action items. Quickly enhance exceptional schemas before highly efficient sources. \\r\\n\",\"log\":\"\",\"revision_timestamp\":\"1287462879\",\"format\":\"1\",\"name\":\"admin\",\"picture\":\"\",\"data\":\"a:0:{}\",\"last_comment_timestamp\":\"1285374480\",\"last_comment_name\":null,\"comment_count\":\"0\",\"taxonomy\":{\"2\":{\"tid\":\"2\",\"vid\":\"1\",\"name\":\"Category 2\",\"description\":\"\",\"weight\":\"0\"}}}]}";
                will(returnValue(json));
            }
        });

        drupalSiteContext.getNodeView("andrupal_recent");
    }

    @Test
    public void testGetNode() throws Exception {
        context.checking(new Expectations() {
            {
                Class<? super Map<String, Object>> type = new TypeToken<Map<String, Object>>() {
                }.getRawType();
                //noinspection unchecked
                one(mockJsonRequestManager).post(with(equal(siteUrl + "/services/json")), with(equal("system.connect")), (Map<String, Object>) with(aNull(type)), with(equals(false)));
                String json = "{\"#error\":false,\"#data\":\"\"}";
                will(returnValue(json));

                one(mockJsonRequestManager).postSigned(
                        with(equal(siteUrl + "/services/json")),
                        with(equal("node.get")),
                        with(IsMapContaining.hasEntry("nid", (Object) 1)),
                        with(equals(false))
                );
                json = "{\"#error\":false,\"#data\":{\"nid\":\"3\",\"type\":\"page\",\"language\":\"\",\"uid\":\"1\",\"status\":\"1\",\"created\":\"1285472651\",\"changed\":\"1287462859\",\"comment\":\"0\",\"promote\":\"0\",\"moderate\":\"0\",\"sticky\":\"0\",\"tnid\":\"0\",\"translate\":\"0\",\"vid\":\"3\",\"revision_uid\":\"1\",\"title\":\"More Content\",\"body\":\"\\u003ca href=\\\"http:\\/\\/google.com\\/\\\"\\u003eVisit Google\\u003c\\/a\\u003e\\r\\n\\r\\nAssertively empower robust resources with multimedia based ROI. Efficiently initiate mission-critical testing procedures without cross-platform e-tailers. Energistically engineer emerging \\\"outside the box\\\" thinking before fully tested process improvements. \\r\\n\\r\\nUniquely provide access to timely collaboration and idea-sharing without backward-compatible schemas. Dynamically integrate compelling bandwidth vis-a-vis compelling results. Competently generate one-to-one ROI after global products.\\r\\nAssertively empower robust resources with multimedia based ROI. Efficiently initiate mission-critical testing procedures without cross-platform e-tailers. Energistically engineer emerging \\\"outside the box\\\" thinking before fully tested process improvements. \\r\\n\\r\\nUniquely provide access to timely collaboration and idea-sharing without backward-compatible schemas. Dynamically integrate compelling bandwidth vis-a-vis compelling results. Competently generate one-to-one ROI after global products.\\r\\nAssertively empower robust resources with multimedia based ROI. Efficiently initiate mission-critical testing procedures without cross-platform e-tailers. Energistically engineer emerging \\\"outside the box\\\" thinking before fully tested process improvements. \\r\\n\\r\\nUniquely provide access to timely collaboration and idea-sharing without backward-compatible schemas. Dynamically integrate compelling bandwidth vis-a-vis compelling results. Competently generate one-to-one ROI after global products.Assertively empower robust resources with multimedia based ROI. Efficiently initiate mission-critical testing procedures without cross-platform e-tailers. Energistically engineer emerging \\\"outside the box\\\" thinking before fully tested process improvements. \\r\\n\\r\\nUniquely provide access to timely collaboration and idea-sharing without backward-compatible schemas. Dynamically integrate compelling bandwidth vis-a-vis compelling results. Competently generate one-to-one ROI after global products.\",\"teaser\":\"\\u003ca href=\\\"http:\\/\\/google.com\\/\\\"\\u003eVisit Google\\u003c\\/a\\u003e\\r\\n\\r\\nAssertively empower robust resources with multimedia based ROI. Efficiently initiate mission-critical testing procedures without cross-platform e-tailers. Energistically engineer emerging \\\"outside the box\\\" thinking before fully tested process improvements. \\r\\n\\r\\nUniquely provide access to timely collaboration and idea-sharing without backward-compatible schemas. Dynamically integrate compelling bandwidth vis-a-vis compelling results. Competently generate one-to-one ROI after global products.\\r\",\"log\":\"\",\"revision_timestamp\":\"1287462859\",\"format\":\"1\",\"name\":\"admin\",\"picture\":\"\",\"data\":\"a:0:{}\",\"last_comment_timestamp\":\"1285472651\",\"last_comment_name\":null,\"comment_count\":\"0\",\"taxonomy\":{\"1\":{\"tid\":\"1\",\"vid\":\"1\",\"name\":\"Category 1\",\"description\":\"\",\"weight\":\"0\"}}}}";
                will(returnValue(json));
            }
        });
        DrupalNode node = drupalSiteContext.getNode(1);
        assertNotNull(node);
    }

    @Test
    public void testGetComment() throws Exception {
        // TODO: implement
        // drupalSiteContext.getComment(2);
    }

    @Test
    public void testSaveComment() throws Exception {

    }

    @Test
    public void testLogin() throws Exception {

        context.checking(new Expectations() {
            {
                Class<? super Map<String, Object>> type = new TypeToken<Map<String, Object>>() {
                }.getRawType();
                //noinspection unchecked
                one(mockJsonRequestManager).post(with(equal(siteUrl + "/services/json")), with(equal("system.connect")), (Map<String, Object>) with(aNull(type)), with(equals(false)));
                String json = "{\"#error\":false,\"#data\":\"\"}";
                will(returnValue(json));
                one(mockJsonRequestManager).postSigned(
                        with(
                                equal(siteUrl + "/services/json")
                        ),
                        with(
                                equal("user.login")
                        ),
                        with(
                                allOf(
                                        IsMapContaining.hasEntry("username", (Object) "test"),
                                        IsMapContaining.hasEntry("password", (Object) "testpass")
                                )
                        ),
                        with(equals(false))
                );
                json = "{\"#error\":false,\"#data\":{\"sessid\":\"e7443fe315fc200c2370bfe7f1be2040\",\"user\":{\"uid\":\"1\",\"name\":\"admin\",\"pass\":\"3e47b75000b0924b6c9ba5759a7cf15d\",\"mail\":\"aaron@workhabit.com\",\"mode\":\"0\",\"sort\":\"0\",\"threshold\":\"0\",\"theme\":\"\",\"signature\":\"\",\"signature_format\":\"0\",\"created\":\"1285372909\",\"access\":\"1288039409\",\"login\":1288043068,\"status\":\"1\",\"timezone\":null,\"language\":\"\",\"picture\":\"\",\"init\":\"aaron@workhabit.com\",\"data\":\"a:0:{}\",\"roles\":{\"2\":\"authenticated user\"}}}}";
                will(returnValue(json));
            }
        });
        DrupalUser drupalUser = drupalSiteContext.login("test", "testpass");
        assertNotNull(drupalUser);
    }

    @Test
    public void testGetTermView() throws Exception {
    }

    @Test
    public void testGetCategoryList() throws Exception {
    }

    @After
    public void tearDown() {
        context.assertIsSatisfied();
    }
}
