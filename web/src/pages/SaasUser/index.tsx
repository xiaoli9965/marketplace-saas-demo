import React, {useRef, useState} from "react";
import ProTable, {ActionType, ProColumns} from "@ant-design/pro-table";
import {PageContainer} from "@ant-design/pro-layout";
import {Button, FormInstance, message} from "antd";
import {ModalForm, ProFormText} from "@ant-design/pro-form";
import {userPage} from "@/services/apis/saas-user-api";
import {useModel} from "@@/plugin-model/useModel";


const SaasUser: React.FC = () => {
  const {initialState} = useModel('@@initialState');

  const formRef = useRef<FormInstance>();
  const tbRef = useRef<ActionType>();

  const [appData, setAppData] = useState({})
  const [modalVisit, setModalVisit] = useState(false);

  const columns: ProColumns[] = [
    {
      dataIndex: 'id',
      title: 'id',
      hideInSearch: true,
    },
    {
      dataIndex: 'username',
      title: '用户名',
      hideInSearch: true,

    },
    {
      dataIndex: 'account',
      title: '账号',
      copyable: true,
      hideInSearch: true,

    },
    {
      dataIndex: 'password',
      title: '密码',
      hideInSearch: true,

    },
    {
      dataIndex: 'role',
      title: '角色',
      hideInSearch: true,
    },
    {
      dataIndex: 'instance',
      title: '所属实例',
      copyable: true
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
        title="新建表单"

        modalProps={{}}
        visible={modalVisit}
        initialValues={appData}
        onVisibleChange={setModalVisit}
        onFinish={async (values) => {
          return true
        }}
      >
        <ProFormText width="md" name="appId" label="app id" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="md" name="appName" label="app name" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="md" name="appKey" label="app_key" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="md" name="ssoId" label="sso_key" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="md" name="ssoKey" label="sso 密钥" rules={[{required: true, message: '此项是必填项'}]}/>
      </ModalForm>
    )
  }

  return (
    <PageContainer>

      <ProTable<API.RuleListItem, API.PageParams>
        rowKey="app_id"
        actionRef={tbRef}
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <AddModal/>
        ]}
        request={async (params) => {
          const ret: R.PageRet = await userPage({instance:initialState.currentUser.instance,...params});
          return {success: true, ...ret};
        }}
        columns={columns}
      />
    </PageContainer>
  )
}


export default SaasUser
