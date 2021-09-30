import React, {useRef, useState} from "react";
import ProTable, {ActionType, ProColumns} from "@ant-design/pro-table";
import {PageContainer} from "@ant-design/pro-layout";
import {Button, FormInstance, message} from "antd";
import {ModalForm, ProFormText} from "@ant-design/pro-form";
import {instanceRenewPage, spiRecordPage} from "@/services/apis/instance-renew-api";
import {action_enum} from "@/common/SpiAction";
import {useModel} from "@@/plugin-model/useModel";


const InstanceRenewList: React.FC = () => {
  const formRef = useRef<FormInstance>();
  const tbRef = useRef<ActionType>();

  const [recordData, setRecordData] = useState({})
  const [modalVisit, setModalVisit] = useState(false);
  const {initialState} = useModel('@@initialState');


  const columns: ProColumns[] = [
    {
      dataIndex: 'id',
      valueType: "index"
    },
    {
      dataIndex: 'instanceId',
      title: '实例id',
      copyable: true,
    },
    {
      dataIndex: 'action',
      title: 'spi操作',
      valueEnum: action_enum,
    },
    {
      dataIndex: 'spec',
      title: '套餐',
      hideInSearch: true,

    },
    {
      dataIndex: 'period',
      title: '有效期',
      hideInSearch: true,

    },
    {
      dataIndex: 'createTime',
      title: '创建时间',
      valueType: "dateTime",
      hideInSearch: true,

    }
  ]

  const AddModal = () => {
    return (
      <ModalForm<{
        name: string;
        company: string;
      }>
        title="表单"

        modalProps={{}}
        visible={modalVisit}
        initialValues={recordData}
        onVisibleChange={setModalVisit}
        onFinish={async (values) => {
          return true
        }}
      >
        <ProFormText width="md" name="instanceId" label="实例id" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="md" name="appId" label="appid" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="md" name="spec" label="套餐" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="md" name="period" label="有效期" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="md" name="thirdUserId" label="青云用户id"/>
        <ProFormText width="xl" name="cloudInfo" label="云平台信息"/>
        <ProFormText width="md" name="createTime" label="创建时间"/>
      </ModalForm>
    )
  }

  return (
    <PageContainer>

      <ProTable<API.RuleListItem, API.PageParams>
        rowKey="id"
        actionRef={tbRef}
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <AddModal/>
        ]}
        request={async (params) => {
          const ret: R.PageRet = await instanceRenewPage({instanceId: initialState.currentUser.instance,...params});
          return {success: true, ...ret};
        }}
        columns={columns}
      />
    </PageContainer>
  )
}

export default InstanceRenewList
