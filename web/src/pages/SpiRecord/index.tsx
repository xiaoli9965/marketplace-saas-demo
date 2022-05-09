import React, {useRef, useState} from "react";
import ProTable, {ActionType, ProColumns} from "@ant-design/pro-table";
import {PageContainer} from "@ant-design/pro-layout";
import {Button, FormInstance, message} from "antd";
import {ModalForm, ProFormText, ProFormTextArea} from "@ant-design/pro-form";
import {spiRecordPage} from "@/services/apis/spi-record-api";
import {action_enum} from "@/common/SpiAction";


const SpiRecord: React.FC = () => {
  const tbRef = useRef<ActionType>();

  const [recordData, setRecordData] = useState({})
  const [modalVisit, setModalVisit] = useState(false);


  const showInfo = (data) => {
    setRecordData(data)
    setModalVisit(true);
  }
  const columns: ProColumns[] = [
    {
      dataIndex: 'id',
      valueType: "index"
    },
    {
      dataIndex: 'action',
      title: '事件',
      valueEnum: action_enum,
    },
    {
      dataIndex: 'primaryId',
      title: 'id',
    },
    {
      dataIndex: 'params',
      title: 'url params',
      hideInSearch: true,
      ellipsis: true,
      copyable: true
    },
    {
      dataIndex: 'req',
      title: 'json params',
      hideInSearch: true,
      ellipsis: true,
      copyable: true
    },
    {
      dataIndex: 'resp',
      title: 'resp',
      hideInSearch: true,
      ellipsis: true,
      copyable: true
    },
    {
      dataIndex: 'createTime',
      title: '创建时间',
      valueType: "dateTime",
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
        <ProFormTextArea width="xl" name="params" label="请求"/>
        <ProFormTextArea width="xl" name="resp" label="响应"/>
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
          const ret: R.PageRet = await spiRecordPage({...params});
          return {success: true, ...ret};
        }}
        columns={columns}
      />
    </PageContainer>
  )
}

export default SpiRecord
