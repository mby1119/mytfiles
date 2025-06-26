import { defineStore } from 'pinia';

export const useDataStore = defineStore('data', {
    state: () => ({
        sharedData: null,
        tempData: {}
    }),
    actions: {
        setSharedData(data) {
            this.sharedData = data;
        },
        setTempData(key, data) {
            this.tempData[key] = data;
        },
        getTempData(key) {
            return this.tempData[key];
        },
        clearTempData(key) {
            if (key) {
                delete this.tempData[key];
            } else {
                this.tempData = {};
            }
        }
    }
});