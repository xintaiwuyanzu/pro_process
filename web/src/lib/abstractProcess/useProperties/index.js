import {reactive, watchEffect} from 'vue-demi'
import {useRouter} from '@u3u/vue-hooks'
import {http} from "@dr/framework/src/plugins/http";
import vue from "vue";

/**
 *用来加载环节或者流程扩展属性
 */
export default () => {
    /**
     * 环节实例属性
     */
    const properties = reactive({})

    const {route} = useRouter()
    const loadProperties = async (taskId) => {
        const {data} = await http().get(`/processTaskInstance/detail?id=${taskId}`)
        return data
    }
    watchEffect(async () => {
        const query = route.value.query
        if (query.taskId) {
            const data = await loadProperties(query.taskId)
            if (data.success) {
                const {proPerties} = data.data
                if (proPerties) {
                    proPerties.forEach(p => {
                        let value = p.value
                        if ('true' === value) {
                            value = true
                        }
                        vue.set(properties, p.name, value)
                    })
                }
            }
        }
    })

    return {properties, loadProperties}
}