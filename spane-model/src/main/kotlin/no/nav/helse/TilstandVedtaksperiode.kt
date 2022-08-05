package no.nav.helse

interface TilstandVedtaksperiode {

    fun hørerTil(vedtaksperiodeId: String?): Boolean

    fun accept(visitor: VedtakFattetVisitor) {}

    fun accept(visitor: VedtaksperiodeForkastetVisitor) {}
}