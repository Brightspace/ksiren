package com.brightspace.ksiren

sealed class PropertyValue

data class StringValue(val stringValue: String) : PropertyValue()

// No point constructing instances when there are only two possibilities.
// No need to be a data class since comparing by identity/reference is sufficient given only two instances.
class BooleanValue private constructor(val booleanValue: Boolean) : PropertyValue() {
	companion object {
		val TRUE = BooleanValue(true)
		val FALSE = BooleanValue(false)
		fun from(b: Boolean) = if (b) TRUE else FALSE
	}
	override fun toString() = booleanValue.toString()
}

data class ArrayValue(val arrayElements: List<PropertyValue>) : PropertyValue()

data class ObjectValue(val objectProperties: Map<String, PropertyValue>) : PropertyValue()
