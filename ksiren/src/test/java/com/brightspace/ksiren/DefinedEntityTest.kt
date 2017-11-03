package com.brightspace.ksiren

import com.brightspace.ksiren.moshi_adapter.KSirenMoshiReaderFactory
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by mpomeroy on 11/3/17.
 */
class DefinedEntityTest {

	class TestEntity1(baseEntity: Entity): DefinedEntity(baseEntity, KSirenEntitySignature(
		listOf("test1"),
		listOf(),
		listOf(),
		listOf()
	)) {
		val classes = baseEntity.classes
	}

	class TestEntity2(baseEntity: Entity): DefinedEntity(baseEntity, KSirenEntitySignature(
		listOf("test2"),
		listOf(),
		listOf(),
		listOf()
	)) {
		val classes = baseEntity.classes
	}

	class TestEntity3(baseEntity: Entity): DefinedEntity(baseEntity, KSirenEntitySignature(
		listOf("test3"),
		listOf(),
		listOf(),
		listOf()
	)) {
		val classes = baseEntity.classes
	}

	@Test
	fun fetchEntityFromMismatchedClass() {
		val json = """{"class": ["test1"], "entities":[{"class":["test2"],"href":"http://example.com.test"}]}"""
		val entity = TestEntity1(Entity.fromJson(json.toKSirenJsonReader()))

		val subEntities: List<TestEntity3> = entity.getSubEntitiesOfType(object: KSirenEntityFetchClient() {
			override fun executeCall(href: String): KSirenJsonReader {
				if (href == "http://example.com.test") {
					return KSirenMoshiReaderFactory()
						.getKSirenJsonReader("""{"class": ["test3"], "rel":["http://x.io/rels/customer"]}""")
				} else {
					throw Exception("Unexpected URL while fetching entity.")
				}
			}
		}, {
			TestEntity3(it)
		})

		assertEquals(listOf("test3"), subEntities[0].classes)
	}

	@Test
	fun fetchEntity() {
		val json = """{"class": ["test1"], "entities":[{"class":["test2"],"href":"http://example.com.test"}]}"""
		val entity = TestEntity1(Entity.fromJson(json.toKSirenJsonReader()))

		val subEntities: List<TestEntity2> = entity.getSubEntitiesOfType(object: KSirenEntityFetchClient() {
			override fun executeCall(href: String): KSirenJsonReader {
				if (href == "http://example.com.test") {
					return KSirenMoshiReaderFactory()
						.getKSirenJsonReader("""{"class": ["test2"], "rel":["http://x.io/rels/customer"]}""")
				} else {
					throw Exception("Unexpected URL while fetching entity.")
				}
			}
		}, {
			TestEntity2(it)
		})

		assertEquals(listOf("test2"), subEntities[0].classes)
	}
}
