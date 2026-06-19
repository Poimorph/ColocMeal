package com.example.colocmeal.domain.utils

import java.text.Normalizer

/**
 * Canonical name normalization shared across grocery items, ingredient catalog and
 * meal-plan auto-add: lowercase + trim + strip diacritics. Used as the dedup key.
 */
fun normalizeName(value: String): String =
    Normalizer.normalize(value.trim().lowercase(), Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(), "")