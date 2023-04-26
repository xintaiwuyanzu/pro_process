<template>
    <el-dialog title="流程启动确认"
               ref="dialog"
               :visible.sync="visible"
               width="60%"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               :show-close="false">
        <el-form :model="form" label-width="120px" ref="form" v-loading="loading">
            <el-form-item prop="processId" label="流程" required>
                <el-select v-model="form.processId" placeholder="请选择要发起的流程" filterable
                           @change="getCurrentProcessDefinition">
                    <el-option v-for="define in processDefinition"
                               :value="define.id"
                               :label="`${define.name||define.key}-${define.version}`"
                               :key="define.id"/>
                </el-select>
            </el-form-item>
            <el-form-item prop="person" label="接收人" required>
                <el-select v-model="form.person" multiple placeholder="请选择接收人" filterable>
                    <el-option v-for="person in currentPersons"
                               :value="person.id"
                               :label="person.userName"
                               :key="person.id"/>
                </el-select>
            </el-form-item>
            <slot :form="form"/>
            <el-form-item prop="comment" label="意见">
                <el-input v-model="form.comment" placeholder="请填写意见" type="textarea"/>
            </el-form-item>
        </el-form>
        <div slot="footer">
            <el-button type="primary" @click="submit" :loading="loading">确 认</el-button>
            <el-button type="info" @click="close" :loading="loading">取 消</el-button>
        </div>
    </el-dialog>
</template>
<script>
import {Message} from "element-ui";

/**
 * 流程相关选择器
 */
export default {
    name: 'processSelect',
    props: {
        /**
         * 弹窗显示状态
         */
        visible: {default: false, type: Boolean},
        /**
         * 可选的流程定义
         */
        processDefinition: {type: Array, default: () => []},
        /**
         * 加载状态
         */
        loading: {type: Boolean, default: false}
    },
    data() {
        return {
            form: {
                processId: '',
                person: [],
                //启动意见
                comment: ''
            },
            //启动环节角色下的人员列表
            currentPersons: [],
        }
    },
    methods: {
        /**
         *提交表单
         * @returns {Promise<void>}
         */
        async submit() {
            const valid = await this.$refs.form.validate()
            if (valid) {
                this.$emit('submit', this.form)
            }
        },
        /**
         * 广播关闭事件
         */
        close() {
            this.$emit('close')
        },
        async $init() {
            // const {data} = await this.$post('/processDefinition/currentOrganisePersons/')
        },
        async getCurrentProcessDefinition(v) {
            this.currentPersons = []
            this.form.person = []
            //根据流程定义id获取启动环节角色下的人员列表
            const {data} = await this.$post('/processDefinition/getPersonByProcessDefinitionId', {
                processDefinitionId: v
            })
            if (data.success) {
                this.currentPersons = data.data
            }
        }
    }
}
</script>