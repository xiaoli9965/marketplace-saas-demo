import React, {useRef, useState} from "react";
import ProTable, {ActionType, ProColumns} from "@ant-design/pro-table";
import {PageContainer} from "@ant-design/pro-layout";
import {Button, FormInstance} from "antd";
import {instancePage} from "@/services/apis/instance-api";
import {ModalForm, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import {action_enum} from "@/common/SpiAction";
import {useModel} from "@@/plugin-model/useModel";


const InstanceList: React.FC = () => {
  const formRef = useRef<FormInstance>();
  const tbRef = useRef<ActionType>();

  const [recordData, setRecordData] = useState({})
  const [modalVisit, setModalVisit] = useState(false);

  const [paramsExt, setParamsExt] = useState<any>();

  const {initialState} = useModel('@@initialState');

  const showInfo = (data) => {
    setRecordData(data)
    setModalVisit(true);
  }
  const expandedRowRender = (recode) => {
    return (
      <ProTable
        columns={[
          {
            dataIndex: 'id',
            valueType: "index"
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
        ]}
        size={"small"}
        search={false}
        options={false}
        rowKey={"id"}
        dataSource={recode?.renews}
        pagination={false}
      />
    );
  }

  const columns: ProColumns[] = [
    {
      dataIndex: 'instanceId',
      title: '实例id',
      copyable: true,
    },
    {
      dataIndex: 'appId',
      title: '应用id',
    },
    {
      dataIndex: 'appName',
      title: '应用',
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
      dataIndex: 'userId',
      title: '用户id',
    },
    {
      dataIndex: 'thirdUserId',
      title: '青云用户id',
      copyable: true,
    },
    {
      dataIndex: 'createTime',
      title: '创建时间',
      valueType: "dateTime",
      hideInSearch: true,
    },
    {
      dataIndex: 'status',
      title: '是否过期',
      render: (_, row) => {
        return row.status == true ? '是' : '否'
      },
      hideInSearch: true,
    },
    {
      dataIndex: 'del',
      title: '已删除',
      render: (_, row) => {
        return row.del == true ? '是' : '否'
      },
      hideInSearch: true,
    },
    {
      title: '操作', key: 'option', align: 'center', valueType: 'option',
      render: (_, row) => [
        <Button type="link" onClick={async () => showInfo(row)}> 查看</Button>
      ]
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
        <ProFormText disabled width="md" name="instanceId" label="实例id" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText disabled width="md" name="appId" label="appid" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText disabled width="md" name="spec" label="套餐" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText disabled width="md" name="period" label="有效期" rules={[{required: true, message: '此项是必填项'}]}/>
        <ProFormText disabled width="md" name="thirdUserId" label="青云用户id"/>
        <ProFormText disabled width="md" name="createTime" label="创建时间"/>
        <ProFormTextArea name="cloudInfo" label="云平台信息"/>
      </ModalForm>
    )
  }

  return (
    <PageContainer>

      <ProTable<API.RuleListItem, API.PageParams>
        rowKey="instanceId"
        actionRef={tbRef}
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <AddModal/>
        ]}
        params={paramsExt}
        request={async (params) => {
          const ret: R.PageRet = await instancePage({instanceId: initialState.currentUser.instance, ...params});
          return {success: true, ...ret};
        }}
        expandable={{expandedRowRender}}
        columns={columns}
      />
    </PageContainer>
  )
}

export default InstanceList
