package org.workhabit.drupal.api.site;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Copyright 2009 - WorkHabit, Inc. - acs
 * Date: Oct 14, 2010, 12:06:33 PM
 */
@SuppressWarnings({"UnusedParameters"})
public interface RequestSigningInterceptor {
    public void sign(String path, String method, Map<String, Object> data) throws NoSuchAlgorithmException;
}