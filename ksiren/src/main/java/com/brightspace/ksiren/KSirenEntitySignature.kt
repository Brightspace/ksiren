package com.brightspace.ksiren

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
class KSirenEntitySignature(
	val requiredClasses: List<String>,
	val requiredActions: List<String>,
	val requiredLinkRels: List<String>,
	val requiredProperties: List<String>) {

	fun validate(entity: Entity) {
		requiredClasses.forEach {
			if (!entity.classes.contains(it)) {
				throw KSirenException.ValidationException("Object is missing required class: ".plus(it))
			}
		}
		requiredActions.forEach {
			if (!entity.actions.keys.contains(it)) {
				throw KSirenException.ValidationException("Object is missing required action: ".plus(it))
			}
		}
		requiredLinkRels.forEach outer@ {
			val rel: String = it
			entity.links.forEach {
				if (it.hasRel(rel)) {
					return@outer
				}
			}

			//if we get here that means some required rel was not found
			throw KSirenException.ValidationException("Object is missing required link rel with rel: ".plus(rel))
		}

		requiredProperties.forEach {
			if (!entity.properties.keys.contains(it)) {
				throw KSirenException.ValidationException("Object is missing required property: ".plus(it))
			}
		}
	}
}
