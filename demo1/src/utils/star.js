import { defineStore } from 'pinia'

export const useCounterStore = defineStore('counter', {
    state: () => ({
        message: ''
    }),
    actions: {
        setMessage(msg) {
            this.message = msg
        }
    }
})