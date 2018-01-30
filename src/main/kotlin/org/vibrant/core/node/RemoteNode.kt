package org.vibrant.core.node

open class RemoteNode(
        val address: String,
        val port: Int
){
    override fun toString(): String {
        return "RemoteNode(address='$address', port=$port)"
    }
}