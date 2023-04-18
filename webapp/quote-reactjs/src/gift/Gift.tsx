import {GiftRecord, GiftType} from "./GiftRecord";
import React from "react";

export type GiftProps = {
    gift: GiftRecord,
    onClickHandler: (gift: GiftRecord) => void
}

export function Gift({gift, onClickHandler}: GiftProps) {

    return <div key={gift.id}
                onClick={() => onClickHandler(gift)}
                className={(gift.isUsed ? "gift-used" : "") + " gift-container"}>
        <img
            className={"gift-icon"}
            alt={gift.type}
            src={getImgSrc(gift)}/>
        <div>
            <h5>
                {getGiftHeader(gift)}
            </h5>
            <p className={"gift-date"}>
                {getDisplayedDate(gift)}
            </p>
        </div>
    </div>;
}

function getGiftHeader(gift: GiftRecord) {
    if (gift.type === GiftType.RANDOM_QUOTE) {
        return "Gagnez une citation aléatoire";
    }
    return ""
}

function getImgSrc(gift: GiftRecord) {
    if (gift.isUsed) {
        return "opened-gift.png";
    }
    if (gift.type === GiftType.RANDOM_QUOTE) {
        return "gift-bronze.png";
    }
    return "gift-icon.png"
}

function getDisplayedDate(gift: GiftRecord) {
    return gift.isUsed ?
        "utilisé le " + new Date(gift.usedAt).toLocaleString() :
        "reçu le " + new Date(gift.createdAt).toLocaleDateString();
}
