import {useEffect, useState} from "react";
import "./App.css";
import {Environment} from "./environment";
import testPerson from "./testPerson.json"

export const testBackend = (): Backend => {
    return {
        person(): Promise<PersonDto> {
            return Promise.resolve(testPerson as unknown as PersonDto);
        },
    };
};

export type Backend = {
    person: () => Promise<PersonDto>
}

export type PersonDto = {
    vedtaksperioder: VedtaksperiodeDto[]
    fnr: string
}

export type VedtaksperiodeDto = {
    subsumsjoner: SubsumsjonDto[]
}

export type SubsumsjonDto = {
    id: string
    versjon: string
    eventName: string
    kilde: string
    versjonAvKode: string
    fødselsnummer: string
    sporing: Map<string, string[]>
    tidsstempel: string
    lovverk: string
    lovverksversjon: string
    paragraf: string
    ledd: number | null
    punktum: number | null
    bokstav: string | null
    input: Map<string, any>
    output: Map<string, any>
    utfall: string
}

function App() {
    const backend: Backend = Environment.isDevelopment
        ? testBackend()
        : testBackend();


    const [person, setPerson] = useState<PersonDto>();

    async function fetchAPI() {
        await fetch("/fnr/10877799145")
            .then((res) => {
                return res.json();
            })
            .then((data) => {
                setPerson(data);
            });
    }

    useEffect(() => {
        backend.person().then(r => setPerson(r));
    }, []);

    return (
        <>
            <h1>no Spane no gain</h1>
            <div>People call me the jarlinator, but you can call me tonight</div>
            <div>
                Liste over vedtaksperioder
                {person
                    ? person.vedtaksperioder.map((vedtaksperiode: VedtaksperiodeDto) => {
                        return <div style={{display: "block"}}>{JSON.stringify(vedtaksperiode)}</div>;
                    })
                    : "fant ingen vedtaksperioder"}
            </div>
        </>
    );
}

export default App;
