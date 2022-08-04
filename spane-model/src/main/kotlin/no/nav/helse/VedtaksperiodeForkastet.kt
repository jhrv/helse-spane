package no.nav.helse;

import java.time.LocalDateTime;

class VedtaksperiodeForkastet  (
            private val id: String,
            private val tidsstempel:LocalDateTime,
            private val fødselsnummer: String,
            private val vedtaksperiodeId: String,
            private val organisasjonsnummer: String,
    ) : TilstandVedtaksperiode {
        override fun hørerTil(vedtaksperiodeId: String?) = this.vedtaksperiodeId == vedtaksperiodeId

        override fun accept(visitor: VedtaksperiodeForkastetVisitor) {
            visitor.visitVedtaksperiodeForkastet(
                    id,
                    tidsstempel,
                    fødselsnummer,
                    vedtaksperiodeId,
                    organisasjonsnummer,

            )
        }

    /*
    vedtaksperiode_forkastet: {"id":"wegfa3728-12hf-43738-38298-uhfqui434743f",
    "eventName":"vedtaksperiodeForkastet",
    "tidsstempel":"2022-08-04T14:41:14.310055386",
    "fodselsnummer":"12345678987",
    "vedtaksperiodeId":"gydgy2q-ewqhiu-wejfn-fwj-wjefwejb",
    "organisasjonsnummer":"123456789"}
     */
}

