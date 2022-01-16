package ml.hyperpotion.garden113

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import ml.hyperpotion.garden113.AndroidWebServer
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.*


class MainActivity : AppCompatActivity() {
    private val port = 4113
    private var running = false
    private var ws: AndroidWebServer = AndroidWebServer(port)

    private fun getIpv4HostAddress(): String? {
        NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
            networkInterface.inetAddresses?.toList()?.find {
                !it.isLoopbackAddress && it is Inet4Address
            }?.let { return it.hostAddress }
        }
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv1: TextView = findViewById(R.id.logView)
        val ip = getIpv4HostAddress()
        if (ip == "") {
            tv1.append("Failed to get IP address\n")
        } else {
            tv1.append("Starting nanoHTTPd on $ip:$port\n")
        }
        if (!this.running) {
            this.running = true
            ws.start()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        ws.stop()
        running = false
    }
}