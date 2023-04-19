export type GiftRecord = {
    id: string,
    type: GiftType,
    createdAt: Date,
    usedAt: Date,
    isUsed: boolean,
}

export enum GiftType {
    RANDOM_QUOTE = "RANDOM_QUOTE",
    GUARANTEE_RANDOM_QUOTE = "GUARANTEE_RANDOM_QUOTE",
    PACK_OF_QUOTE = "PACK_OF_QUOTE",
}