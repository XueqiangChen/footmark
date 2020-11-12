package com.xueqiang.footmark.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticObjectInit {

	private static final Logger logger = LoggerFactory.getLogger(StaticObjectInit.class);

	public static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();
}
