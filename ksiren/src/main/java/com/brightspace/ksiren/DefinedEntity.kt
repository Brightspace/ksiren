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
abstract class DefinedEntity(val entity: Entity, signature: KSirenEntitySignature) {

	val subEntities: MutableList<Entity> = entity.entities.toMutableList()

	init {
		//Will throw a ValidationException if the Entity passed does not conform to the signature.
		signature.validate(entity)
	}

	/**
	 * Fetches all Entities of type T from the list of sub entities. If the entity isn't embedded we
	 * use the passed KSirenEntityFetchClient to fetch the complete entity before parsing.
	 *
	 * WARNING: This function uses the network, and will block on the thread it is called from.
	 */
	fun <T:DefinedEntity>getSubEntitiesOfType(client: KSirenEntityFetchClient, factory: (Entity) -> T): List<T> {
		val resultArr: MutableList<T> = mutableListOf()

		for (i in subEntities.indices) {
			val href: String? = entity.entities[i].href

			if (href != null) {
				subEntities[i] = Entity.fromJson(client.executeCall(href))
			}

			try {
				val entity:T = factory(subEntities[i])
				resultArr.add(entity)
			} catch (e: KSirenException.ValidationException) {
				//Do, nothing, this sub entity just doesn't match the requested type
			}
		}
		return resultArr
	}
}
