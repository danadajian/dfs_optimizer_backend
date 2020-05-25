import {findDOMNode} from "react-dom";
import html2canvas from "html2canvas";
import {downloadImage} from "./downloadImage";

export const handleExportLineup = async (navigator: any, componentRef: any) => {
    const lineupGrid: any = findDOMNode(componentRef.current);
    const canvas = await html2canvas(lineupGrid, {
        backgroundColor: null,
        scrollY: -window.scrollY,
        useCORS: true,
    });

    if (navigator.share) {
        const args: any = (blob: Blob) => ({
            title: 'Share Optimal Lineup',
            blob,
            mimeType: 'image/png'
        })
        canvas.toBlob(blob => navigator.share(args(blob)), 'image/png');
    } else {
        const dataUrl = canvas.toDataURL('image/png', 1.0);
        downloadImage(dataUrl, 'lineup.png');
    }
}
