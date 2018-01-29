package org.vibrant.base.http


import com.github.kittinunf.fuel.httpPost
import io.javalin.Javalin
import mu.KotlinLogging
import org.vibrant.core.node.AbstractPeer
import org.vibrant.core.node.RemoteNode
import java.io.ByteArrayInputStream

abstract class HTTPPeer(val port: Int, val config: HTTPPeerConfig): AbstractPeer(){

    @Suppress("unused")
    protected val logger = KotlinLogging.logger{}

    private val server = createServer()


    internal var started = false

    private fun createServer(): Javalin {
        return Javalin
                .create()
                .post(config.endpoint, { ctx ->
                    ctx.result(
                            ByteArrayInputStream(
                                    this.handleRequest(Request(
                                            request = ctx.request(),
                                            body = ctx.bodyAsBytes(),
                                            headers = ctx.headerMap()
                                    ))
                            )
                    )
                })
                .port(port)
    }

    protected abstract fun handleRequest(request: Request): ByteArray

    override fun start() {
        this.started = true
        server.start()
    }

    override fun stop() {
        this.started = false
        server.stop()
    }


    override fun request(byteArray: ByteArray, remoteNode: RemoteNode): ByteArray {
        val(_, _, result) =  "http://${remoteNode.address}:${remoteNode.port}/${config.endpoint}"
                .httpPost()
                .header("peer-port" to port)
                .body(String(byteArray))
                .response()

        return result.get()
    }

}