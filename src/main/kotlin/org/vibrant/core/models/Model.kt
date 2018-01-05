package org.vibrant.core.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

/***
 * Base model class
 * @property _type _type, needed for serialization and deserialization
 */

abstract class Model{
//    abstract protected val _type: String
}