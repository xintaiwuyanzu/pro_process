/**
 * 抽象弹窗父类
 */
export default {
    props: {
        /**
         * 弹窗标题
         */
        title: {type: String},
        /**
         * 弹窗打开回调
         */
        beforeOpen: {type: Function},
        /**
         * 保存前拦截
         */
        beforeSave: {type: Function}
    },
    data() {
        return {
            //弹窗是否显示
            dialogVisible: false,
            //弹窗内数据是否正在加载
            dialogLoading: false,
            //弹窗内置表单对象
            dialogForm: {},
            //默认弹窗标题
            defaultDialogTitle: ''
        }
    },
    methods: {
        /**
         *打开弹窗
         */
        async open() {
            if (this.$refs.form) {
                this.$refs.form.resetFields()
            }
            this.dialogLoading = true
            /**
             * 弹窗打开时回调
             */
            if (this.beforeOpen) {
                const result = await this.beforeOpen(this.dialogForm)
                if (result === false) {
                    this.dialogLoading = false
                    return
                }
            }
            this.dialogVisible = true
            //弹窗表单数据初始化
            if (this.$initDialogForm) {
                await this.$initDialogForm()
            }
            this.dialogLoading = false
        },
        /**
         * 广播关闭事件
         */
        close() {
            this.dialogVisible = false
            this.$emit('close')
        },
    },
    computed: {
        /**
         * 动态计算弹窗标题
         */
        dialogTitle() {
            return this.title || this.defaultDialogTitle
        }
    }
}