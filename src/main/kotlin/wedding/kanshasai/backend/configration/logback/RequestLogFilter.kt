package wedding.kanshasai.backend.configration.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply

class RequestLogFilter : Filter<ILoggingEvent>() {
    private var isRequest = true

    override fun decide(event: ILoggingEvent): FilterReply {
        return if (event.message.contains("Request message")) {
            if (isRequest) FilterReply.ACCEPT else FilterReply.DENY
        } else {
            if (isRequest) FilterReply.DENY else FilterReply.ACCEPT
        }
    }

    fun setIsRequest(isRequest: Boolean) {
        this.isRequest = isRequest
    }
}
