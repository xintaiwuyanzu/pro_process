import {registerField} from "@dr/framework/src/components/formRender";
import {computeArgs} from "@dr/framework/src/components/formRender/fieldRender";

export default (vue) => {
    //注册常用意见自定义表单
    registerField('opinion',
        (props, context) => {
            const opinion = vue.component('processOpinion')
            return context.$createElement(opinion, computeArgs(props, context, true))
        }
    )
}
