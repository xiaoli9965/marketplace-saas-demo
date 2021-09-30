import React, {useRef, useState} from "react";
import ProTable, {ActionType, ProColumns} from "@ant-design/pro-table";
import {PageContainer} from "@ant-design/pro-layout";
import {Button, FormInstance, message} from "antd";
import {PlusOutlined} from "@ant-design/icons";
import {appPage, addApp, delApp} from "@/services/apis/third-app-api";
import ProForm, {ModalForm, ProFormText} from "@ant-design/pro-form";

const AppList: React.FC = () => {
  const formRef = useRef<FormInstance>();
  const tbRef = useRef<ActionType>();

  const [appData, setAppData] = useState({})
  const [modalVisit, setModalVisit] = useState(false);

  const showInfo = (data) => {
    setAppData(data)
    setModalVisit(true);
  }

  const deletApp = async (id) => {
    try {
      await delApp(id)
      tbRef.current?.reload()
      return
    } catch (e) {
      message.error(`报错了: ${e.message}`);
    }

  }

  const columns: ProColumns[] = [
    {
      dataIndex: 'appId',
      title: 'appId',
      copyable: true,
    },
    {
      dataIndex: 'appName',
      title: 'appName',
      copyable: true,
      hideInSearch:true,
    },
    {
      dataIndex: 'appKey',
      title: 'appKey',
      copyable: true
    },
    {
      dataIndex: 'ssoKey',
      title: 'SSO密钥',
      copyable: true,
      hideInSearch:true,
    },
    {
      title: '操作', key: 'option', align: 'center', valueType: 'option',
      render: (_, row) => [
        <Button type="link" onClick={async () => showInfo(row)}> 查看</Button>,
        <Button type="link" onClick={async () => deletApp(row.appId)}> 删除</Button>
      ]
    }
  ]

  const AddModal = () => {
    return (
      <ModalForm<{
        name: string;
        company: string;
      }>
        title="新建表单"

        trigger={
          <Button type="primary">
            <PlusOutlined/>
            新建
          </Button>
        }
        modalProps={{}}
        visible={modalVisit}
        initialValues={appData}
        onVisibleChange={setModalVisit}
        onFinish={async (values) => {
          try {
            await formRef.current?.validateFields()
          } catch (e) {
            return false
          }

          try {
            const ret = await addApp(values)
            if (ret.code === 200) {
              tbRef.current?.reload()
              return true
            }
          } catch (e) {
            message.error(`报错了: ${e.message}`);
          }

          return false
        }}
      >
        <ProFormText width="xl" name="appId" label="app id" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="xl" name="appName" label="app name" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="xl" name="appKey" label="app_key" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText width="xl" name="ssoKey" label="sso 密钥" rules={[{required: true, message: '此项是必填项'}]}/>
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
          const ret: R.PageRet = await appPage({...params});
          return {success: true, ...ret};
        }}
        columns={columns}
      />
    </PageContainer>
  )
}

export default AppList
