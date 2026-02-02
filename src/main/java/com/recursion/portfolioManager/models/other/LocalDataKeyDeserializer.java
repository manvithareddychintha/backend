package com.recursion.portfolioManager.models.other;

import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.KeyDeserializer;

import java.time.LocalDate;

public class LocalDataKeyDeserializer extends KeyDeserializer {
    @Override
    public LocalDate deserializeKey(String key, DeserializationContext ctxt) {
        return LocalDate.parse(key); // "2026-01-30" → LocalDate
    }
}
