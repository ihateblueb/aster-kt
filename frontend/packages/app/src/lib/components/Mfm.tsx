import type {MfmNode} from "mfm-js";
import * as mfm from 'mfm-js'
import './Mfm.scss'
import * as React from "react";

function Mfm(
    {text, simple = false, emoji = []}:
    { text?: string, simple?: boolean, emoji?: any[] }
) {
    let result: Array<React.ReactNode> = []

    React.useEffect(() => {
        render()
    })

    render()

    function render() {
        if (text === undefined) return undefined

        let mfmTree = simple ? mfm.parseSimple(text) : mfm.parse(text);
        console.debug('[Mfm Tree]', mfmTree)

        renderChildren(mfmTree)
    }

    function renderChildren(children: MfmNode[], depth: number = 0) {
        for (const node of children) {
            renderElement(node, depth)
            if (node.children) renderChildren(node.children, depth + 1)
        }
    }

    function renderElement(e: MfmNode, depth: number = 0) {
        switch (e.type) {
            case 'text':
                result.push(
                    <span dangerouslySetInnerHTML={{__html: e.props.text.replace(/(\r\n|\n|\r)/g, '\n')}}/>
                )
                if (e.children) renderChildren(e.children, depth + 1)
                break
        }
    }


    return (
        <>
            {result}
        </>
    )
}

export default Mfm
