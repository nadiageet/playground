import {useEffect, useState} from 'react';


export function getValueFromStorage<T>(key: string, defaultValue: T | null = null) {
    const storedValue = localStorage.getItem(key);
    if (storedValue === null) {
        return defaultValue;
    }
    try {
        return JSON.parse(storedValue) as T;
    } catch (error) {
        console.error(`couldn't parse JSON object for ${key}`)
        return defaultValue;
    }
}

function updateLocalStorage<T>(value: T | null, key: string) {
    if (value === null) {
        localStorage.removeItem(key);
    } else {
        localStorage.setItem(key, JSON.stringify(value));
    }
}

export default function useLocalStorage<T>(key: string, defaultValue: T | null = null): [T | null, React.Dispatch<React.SetStateAction<T | null>>] {
    console.log("retrieving key" + key);
    const [value, setValue] = useState<T | null>(() => {
        console.log("HERE")
        const valueFromStorage = getValueFromStorage(key, defaultValue);
        console.log("initial value from local storage : " + valueFromStorage);
        return valueFromStorage;
    });

    useEffect(() => {
        updateLocalStorage(value, key);
    }, [key, value]);

    console.log({key, value})

    return [value, setValue];
}

export type LocalStorageContainer<T> = {
    value?: T | null;
}