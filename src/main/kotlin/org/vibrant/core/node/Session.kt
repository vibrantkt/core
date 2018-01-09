package org.vibrant.core.node

abstract class Session<Package: UDPSessionPeer.Communication.CommunicationPackage>(val remoteNode: RemoteNode, val request: Package) {
    abstract fun handle(response: Package)
}