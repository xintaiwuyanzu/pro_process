export default '<?xml version="1.0" encoding="UTF-8"?>\n' +
'<bpmn:definitions xmlns:bpmn="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xmlns:camunda="http://camunda.org/schema/1.0/bpmn" id="Definitions_04ft061" targetNamespace="http://bpmn.io/schema/bpmn" exporter="Camunda Modeler" exporterVersion="3.4.1">\n' +
'  <bpmn:process id="cskh_bnkh" name="处室KPI半年申报" isExecutable="true">\n' +
'    <bpmn:documentation>处室半年KPI事项申报</bpmn:documentation>\n' +
'    <bpmn:extensionElements>\n' +
'      <camunda:properties>\n' +
'        <camunda:property name="formUrl" value="/bhkh/cskh/quarterlyPlan" />\n' +
'        <camunda:property name="editformUrl" value="/bhkh/cskh/quarterlyPlan/edit" />\n' +
'        <camunda:property name="handleUrl" value="/bhkh/cskh/quarterlyPlan/handle" />\n' +
'      </camunda:properties>\n' +
'    </bpmn:extensionElements>\n' +
'    <bpmn:startEvent id="StartEvent_1" name="开始">\n' +
'      <bpmn:outgoing>SequenceFlow_0ty34f9</bpmn:outgoing>\n' +
'    </bpmn:startEvent>\n' +
'    <bpmn:sequenceFlow id="SequenceFlow_0ty34f9" name="保存" sourceRef="StartEvent_1" targetRef="csbnkh_cscaogao" />\n' +
'    <bpmn:sequenceFlow id="SequenceFlow_1enycq4" name="提交" sourceRef="csbnkh_cscaogao" targetRef="csbnkh_csshenhe" />\n' +
'    <bpmn:sequenceFlow id="SequenceFlow_0vzyu1u" name="发送" sourceRef="csbnkh_csshenhe" targetRef="csbnkh_csdafen" />\n' +
'    <bpmn:sequenceFlow id="SequenceFlow_0drl3ua" name="发送" sourceRef="csbnkh_csdafen" targetRef="csbnkh_csbeian" />\n' +
'    <bpmn:endEvent id="EndEvent_14crexs" name="结束">\n' +
'      <bpmn:incoming>SequenceFlow_1usomj5</bpmn:incoming>\n' +
'    </bpmn:endEvent>\n' +
'    <bpmn:sequenceFlow id="SequenceFlow_1usomj5" name="办结" sourceRef="csbnkh_csbeian" targetRef="EndEvent_14crexs" />\n' +
'    <bpmn:userTask id="csbnkh_cscaogao" name="草稿">\n' +
'      <bpmn:extensionElements>\n' +
'        <camunda:properties>\n' +
'          <camunda:property name="btn_save" value="保存" />\n' +
'          <camunda:property name="btn_commit" value="提交" />\n' +
'          <camunda:property name="btn_return" value="返回" />\n' +
'        </camunda:properties>\n' +
'      </bpmn:extensionElements>\n' +
'      <bpmn:incoming>SequenceFlow_0ty34f9</bpmn:incoming>\n' +
'      <bpmn:outgoing>SequenceFlow_1enycq4</bpmn:outgoing>\n' +
'    </bpmn:userTask>\n' +
'    <bpmn:userTask id="csbnkh_csshenhe" name="单位考核办审核">\n' +
'      <bpmn:extensionElements>\n' +
'        <camunda:properties>\n' +
'          <camunda:property name="btn_send" value="审核通过(发送)" />\n' +
'          <camunda:property name="btn_th" value="审核未通过(退回)" />\n' +
'        </camunda:properties>\n' +
'      </bpmn:extensionElements>\n' +
'      <bpmn:incoming>SequenceFlow_1enycq4</bpmn:incoming>\n' +
'      <bpmn:outgoing>SequenceFlow_0vzyu1u</bpmn:outgoing>\n' +
'    </bpmn:userTask>\n' +
'    <bpmn:userTask id="csbnkh_csdafen" name="单位领导打分">\n' +
'      <bpmn:extensionElements>\n' +
'        <camunda:properties>\n' +
'          <camunda:property name="btn_dafen" value="打分" />\n' +
'          <camunda:property name="btn_th" value="退回" />\n' +
'        </camunda:properties>\n' +
'      </bpmn:extensionElements>\n' +
'      <bpmn:incoming>SequenceFlow_0vzyu1u</bpmn:incoming>\n' +
'      <bpmn:outgoing>SequenceFlow_0drl3ua</bpmn:outgoing>\n' +
'    </bpmn:userTask>\n' +
'    <bpmn:userTask id="csbnkh_csbeian" name="备案">\n' +
'      <bpmn:extensionElements>\n' +
'        <camunda:properties>\n' +
'          <camunda:property name="btn_banjie" value="备案" />\n' +
'          <camunda:property name="btn_th" value="退回" />\n' +
'        </camunda:properties>\n' +
'      </bpmn:extensionElements>\n' +
'      <bpmn:incoming>SequenceFlow_0drl3ua</bpmn:incoming>\n' +
'      <bpmn:outgoing>SequenceFlow_1usomj5</bpmn:outgoing>\n' +
'    </bpmn:userTask>\n' +
'  </bpmn:process>\n' +
'  <bpmndi:BPMNDiagram id="BPMNDiagram_1">\n' +
'    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="cskh_bnkh">\n' +
'      <bpmndi:BPMNShape id="_BPMNShape_StartEvent_2" bpmnElement="StartEvent_1">\n' +
'        <dc:Bounds x="152" y="102" width="36" height="36" />\n' +
'        <bpmndi:BPMNLabel>\n' +
'          <dc:Bounds x="159" y="145" width="22" height="14" />\n' +
'        </bpmndi:BPMNLabel>\n' +
'      </bpmndi:BPMNShape>\n' +
'      <bpmndi:BPMNEdge id="SequenceFlow_0ty34f9_di" bpmnElement="SequenceFlow_0ty34f9">\n' +
'        <di:waypoint x="188" y="120" />\n' +
'        <di:waypoint x="250" y="120" />\n' +
'        <bpmndi:BPMNLabel>\n' +
'          <dc:Bounds x="208" y="102" width="22" height="14" />\n' +
'        </bpmndi:BPMNLabel>\n' +
'      </bpmndi:BPMNEdge>\n' +
'      <bpmndi:BPMNEdge id="SequenceFlow_1enycq4_di" bpmnElement="SequenceFlow_1enycq4">\n' +
'        <di:waypoint x="350" y="120" />\n' +
'        <di:waypoint x="410" y="120" />\n' +
'        <bpmndi:BPMNLabel>\n' +
'          <dc:Bounds x="369" y="102" width="22" height="14" />\n' +
'        </bpmndi:BPMNLabel>\n' +
'      </bpmndi:BPMNEdge>\n' +
'      <bpmndi:BPMNEdge id="SequenceFlow_0vzyu1u_di" bpmnElement="SequenceFlow_0vzyu1u">\n' +
'        <di:waypoint x="510" y="120" />\n' +
'        <di:waypoint x="570" y="120" />\n' +
'        <bpmndi:BPMNLabel>\n' +
'          <dc:Bounds x="529" y="102" width="22" height="14" />\n' +
'        </bpmndi:BPMNLabel>\n' +
'      </bpmndi:BPMNEdge>\n' +
'      <bpmndi:BPMNEdge id="SequenceFlow_0drl3ua_di" bpmnElement="SequenceFlow_0drl3ua">\n' +
'        <di:waypoint x="670" y="120" />\n' +
'        <di:waypoint x="740" y="120" />\n' +
'        <bpmndi:BPMNLabel>\n' +
'          <dc:Bounds x="694" y="102" width="22" height="14" />\n' +
'        </bpmndi:BPMNLabel>\n' +
'      </bpmndi:BPMNEdge>\n' +
'      <bpmndi:BPMNShape id="EndEvent_14crexs_di" bpmnElement="EndEvent_14crexs">\n' +
'        <dc:Bounds x="912" y="102" width="36" height="36" />\n' +
'        <bpmndi:BPMNLabel>\n' +
'          <dc:Bounds x="919" y="145" width="22" height="14" />\n' +
'        </bpmndi:BPMNLabel>\n' +
'      </bpmndi:BPMNShape>\n' +
'      <bpmndi:BPMNEdge id="SequenceFlow_1usomj5_di" bpmnElement="SequenceFlow_1usomj5">\n' +
'        <di:waypoint x="840" y="120" />\n' +
'        <di:waypoint x="912" y="120" />\n' +
'        <bpmndi:BPMNLabel>\n' +
'          <dc:Bounds x="865" y="102" width="22" height="14" />\n' +
'        </bpmndi:BPMNLabel>\n' +
'      </bpmndi:BPMNEdge>\n' +
'      <bpmndi:BPMNShape id="UserTask_1ats3nt_di" bpmnElement="csbnkh_cscaogao">\n' +
'        <dc:Bounds x="250" y="80" width="100" height="80" />\n' +
'      </bpmndi:BPMNShape>\n' +
'      <bpmndi:BPMNShape id="UserTask_1k5kea4_di" bpmnElement="csbnkh_csshenhe">\n' +
'        <dc:Bounds x="410" y="80" width="100" height="80" />\n' +
'      </bpmndi:BPMNShape>\n' +
'      <bpmndi:BPMNShape id="UserTask_0k6os7z_di" bpmnElement="csbnkh_csdafen">\n' +
'        <dc:Bounds x="570" y="80" width="100" height="80" />\n' +
'      </bpmndi:BPMNShape>\n' +
'      <bpmndi:BPMNShape id="UserTask_1n22vq5_di" bpmnElement="csbnkh_csbeian">\n' +
'        <dc:Bounds x="740" y="80" width="100" height="80" />\n' +
'      </bpmndi:BPMNShape>\n' +
'    </bpmndi:BPMNPlane>\n' +
'  </bpmndi:BPMNDiagram>\n' +
'</bpmn:definitions>'
