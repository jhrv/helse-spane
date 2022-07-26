package no.nav.helse

import no.nav.helse.SporingEnum.*
import java.time.ZonedDateTime

class Subsumsjon(
    private val id: String,
    private val versjon: String,
    private val eventName: String = "subsumsjon",
    private val kilde: String,
    private val versjonAvKode: String,
    private val fødselsnummer: String,
    private val sporing: Map<String, Any>,
    private val tidsstempel: ZonedDateTime,
    private val lovverk: String,
    private val lovverksversjon: String,
    private val paragraf: String,
    private val ledd: Int? = null,
    private val punktum: Int? = null,
    private val bokstav: String? = null,
    private val input: Map<String, Any>,
    private val output: Map<String, Any>,
    private val utfall: String,
) {
    internal companion object {
        fun List<Subsumsjon>.finnAlle(paragraf: String) = this.filter { it.paragraf == paragraf }
        fun MutableList<Subsumsjon>.finnAlleUtenSøknadId() = this.filter { it.sporing["soknad"] == null }.toMutableList()


        fun List<Subsumsjon>.sorterPåTid() = this.sortedBy { it.tidsstempel }


        fun MutableList<Subsumsjon>.hentIder(): Map<String, List<String>> {
            val result = mutableMapOf<String, List<String>>()
            var sykmeldingIder = mutableListOf<String>()
            var søknadIder = mutableListOf<String>()
            var vedtaksperiodeIder = mutableListOf<String>()

            forEach {
                when(it.finnSøkeParameter()){
                    VEDTAKSPERIODE -> {
                        vedtaksperiodeIder.add(it.sporing[VEDTAKSPERIODE.navn] as String)
                        søknadIder.add(it.sporing[SØKNAD.navn] as String)
                        sykmeldingIder.add(it.sporing[SYKMELDING.navn] as String)
                    }
                    SØKNAD -> {
                        søknadIder.add(it.sporing[SØKNAD.navn] as String)
                        sykmeldingIder.add(it.sporing[SYKMELDING.navn] as String)
                    }
                    SYKMELDING -> {
                        sykmeldingIder.add(it.sporing[SYKMELDING.navn] as String)
                    }

                }
            }
            result += SYKMELDING.navn to sykmeldingIder
            result += SØKNAD.navn to søknadIder
            result += VEDTAKSPERIODE.navn to vedtaksperiodeIder
            return result
        }



        fun MutableList<Subsumsjon>.erRelevant(subsumsjon: Subsumsjon, søk : SporingEnum): Boolean {
            this.forEach {
                if(it.sporing[søk.navn] == subsumsjon.sporing[søk.navn]) {
                    return true
                }
            }
            return false
        }






        fun MutableList<Subsumsjon>.hentSubsMedID(subsumsjon: Subsumsjon, søk : SporingEnum): MutableList<Subsumsjon> {
            val subsumsjoner = mutableListOf<Subsumsjon>()
            this.forEach {
                if (it.sporing[søk.navn] == subsumsjon.sporing[søk.navn]){
                    subsumsjoner += it
                }
            }
            subsumsjoner += subsumsjon
            return subsumsjoner
        }


    }
    fun sjekkEierskap(søk : SporingEnum, ider : List<String>) :Boolean = ider.contains(sporing[søk.navn])

    fun finnSøkeParameter(): SporingEnum {
        return if (sporing["vedtaksperiode"] != null)  {
            VEDTAKSPERIODE
        } else if (sporing["soknad"] != null) {
            SØKNAD
        } else {
            SYKMELDING
        }
    }

    fun accept(visitor: VedtaksperiodeVisitor) {
        visitor.visitSubsumsjon(
            id,
            versjon,
            eventName,
            kilde,
            versjonAvKode,
            fødselsnummer,
            sporing,
            tidsstempel,
            lovverk,
            lovverksversjon,
            paragraf,
            ledd,
            punktum,
            bokstav,
            input,
            output,
            utfall
        )
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Subsumsjon

        // TODO: Er det noe poeng i å refaktorere denne koden?
        if (id != other.id) return false
        if (versjon != other.versjon) return false
        if (eventName != other.eventName) return false
        if (kilde != other.kilde) return false
        if (versjonAvKode != other.versjonAvKode) return false
        if (fødselsnummer != other.fødselsnummer) return false
        if (sporing != other.sporing) return false
        if (tidsstempel != other.tidsstempel) return false
        if (lovverk != other.lovverk) return false
        if (lovverksversjon != other.lovverksversjon) return false
        if (paragraf != other.paragraf) return false
        if (ledd != other.ledd) return false
        if (punktum != other.punktum) return false
        if (bokstav != other.bokstav) return false
        if (input != other.input) return false
        if (output != other.output) return false
        if (utfall != other.utfall) return false

        return true
    }

    override fun hashCode(): Int {
        // TODO: Vil ikke denne bare overskrive resultatet for hver linje?
        // Holder det å bare bruke den siste linjen?
        var result = id.hashCode()
        result = 31 * result + versjon.hashCode()
        result = 31 * result + eventName.hashCode()
        result = 31 * result + kilde.hashCode()
        result = 31 * result + versjonAvKode.hashCode()
        result = 31 * result + fødselsnummer.hashCode()
        result = 31 * result + sporing.hashCode()
        result = 31 * result + tidsstempel.hashCode()
        result = 31 * result + lovverk.hashCode()
        result = 31 * result + lovverksversjon.hashCode()
        result = 31 * result + paragraf.hashCode()
        result = 31 * result + (ledd ?: 0)
        result = 31 * result + (punktum ?: 0)
        result = 31 * result + (bokstav?.hashCode() ?: 0)
        result = 31 * result + input.hashCode()
        result = 31 * result + output.hashCode()
        result = 31 * result + utfall.hashCode()
        return result
    }

}
