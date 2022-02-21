/**
 * 环节任务容器
 */
import abstractTask from "./abstractTask";
import './styles.scss'
import sendBack from "./sendBack";
import sendNext from "./sendNext";
import endProcess from "./endProcess";

/**
 * 默认slot
 * @type {{endProcess: {name: string}, sendNext: {name: string}, sendBack: {name: string}}}
 */
const defaultSlots = {sendNext, sendBack, endProcess}

export default {
    name: "taskContainer",
    extends: abstractTask,
    render() {
        const directives = [{name: "loading", value: this.loading}]
        const child = []
        const scopedSlots = this.$scopedSlots
        const proPerties = this.taskInstance.proPerties
        const listener = this.$listeners
        if (proPerties) {
            proPerties.forEach(({name, value}) => {
                if (name.startsWith('$btn-')) {
                    name = name.substring(5)
                }
                try {
                    //自定义参数
                    value = JSON.parse(value)
                } catch {
                }

                if (scopedSlots[name]) {
                    const params = {
                        id: name,
                        on: listener,
                        params: value,
                        taskInstance: this.taskInstance,
                        taskDefinitionId: this.taskDefinitionId
                    }
                    child.push(scopedSlots[name](params))
                } else if (defaultSlots[name]) {
                    const component = defaultSlots[name]
                    child.push((<component
                        id={name}
                        on={listener}
                        scopedSlots={scopedSlots}
                        params={value}
                        taskInstanceId={this.taskInstanceId}
                        taskInstance={this.taskInstance}/>))
                }
            })
        }
        return (
            <section class="taskContainer" directives={directives}>
                {child}
            </section>
        )
    }
}