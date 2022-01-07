/**
 * todo 这里可以修改前端编辑页面功能
 */
export default class popMenuProvider {
    constructor() {
        //contextPad.registerProvider(900, this);
    }

    getContextPadEntries() {
        return function (entries) {
            //这里够修改 默认自定义弹窗控件
            if (entries['append.append-task']) {
                entries['append.append-task'].className = 'bpmn-icon-user-task'
            }
            return entries;
        }
    }
}
popMenuProvider.$inject = ['contextPad']