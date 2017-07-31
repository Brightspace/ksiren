package com.brightspace.ksiren

import com.squareup.moshi.JsonReader

/**
 * Copyright 2017 D2L Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class Link(
        val rels: List<String>,
        val classes: List<String> = listOf(),
        val href: String,
        val title: String?,
        val type: String?) : JsonSerializable {

    companion object {

        fun fromJson(reader: KSirenJsonReader): Link {
            val rels: MutableList<String> = mutableListOf()
            val classes: MutableList<String> = mutableListOf()
            var href: String = ""
            var title: String? = null
            var type: String? = null

            reader.beginObject()
            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "rel" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            rels.add(reader.nextString())
                        }
                        reader.endArray()
                    }
                    "class" -> {
                        reader.beginArray()
                        while (reader.hasNext()) {
                            classes.add(reader.nextString())
                        }
                        reader.endArray()
                    }
                    "href" -> {
                        href = reader.nextString()
                    }
                    "title" -> {
                        title = reader.nextString()
                    }
                    "type" -> {
                        type = reader.nextString()
                    }

                }
            }
            reader.endObject()
            val finishedLink: Link = Link(rels, classes, href, title, type)
            return validate(finishedLink)
        }

        fun validate(obj: Link): Link {
            if (obj.rels.isEmpty()) {
                throw KSirenException.ValidationException("Links must contain at least one rel.")
            }
            if (obj.href == "") {
                throw KSirenException.ValidationException("Link must have an href.")
            }

            return obj
        }
    }

    fun hasRel(vararg rel: String): Boolean {
        return rels.containsAll(rel.toList())
    }

    override fun toJson(): CharSequence {
        return JsonUtils.toJson(this)
    }
}
