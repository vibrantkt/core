package org.vibrant.core.node

abstract class Session<B, Package: UDPSessionPeer.Communication.CommunicationPackage<B>>(val remoteNode: RemoteNode, val request: Package) {
    abstract fun handle(response: Package)
}