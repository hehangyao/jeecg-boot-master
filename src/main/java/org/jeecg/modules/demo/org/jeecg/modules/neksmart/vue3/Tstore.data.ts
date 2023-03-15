import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';

export const columns: BasicColumn[] = [
    {
    title: '店铺名',
    dataIndex: 'name'
   },
   {
    title: 'imgUrls',
    dataIndex: 'imgUrls'
   },
   {
    title: '地址',
    dataIndex: 'address'
   },
   {
    title: '电话',
    dataIndex: 'mobile'
   },
   {
    title: '店铺开始时间',
    dataIndex: 'beginTime'
   },
   {
    title: '店铺结束时间',
    dataIndex: 'endTime'
   },
   {
    title: '店铺描述',
    dataIndex: 'description'
   },
   {
    title: '经度',
    dataIndex: 'longitude'
   },
   {
    title: '维度',
    dataIndex: 'latitude'
   },
   {
    title: 'remark',
    dataIndex: 'remark'
   },
   {
    title: '0:失效 1:有效',
    dataIndex: 'isDelete'
   },
];

export const searchFormSchema: FormSchema[] = [
 {
    label: '店铺名',
    field: 'name',
    component: 'Input'
  },
 {
    label: 'imgUrls',
    field: 'imgUrls',
    component: 'Input'
  },
];

export const formSchema: FormSchema[] = [
  // TODO 主键隐藏字段，目前写死为ID
  {label: '', field: 'id', component: 'Input', show: false},
  {
    label: '店铺名',
    field: 'name',
    component: 'Input',
  },
  {
    label: 'imgUrls',
    field: 'imgUrls',
    component: 'Input',
  },
  {
    label: '地址',
    field: 'address',
    component: 'Input',
  },
  {
    label: '电话',
    field: 'mobile',
    component: 'Input',
  },
  {
    label: '店铺开始时间',
    field: 'beginTime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      valueFormat: 'YYYY-MM-DD hh:mm:ss',
    },
  },
  {
    label: '店铺结束时间',
    field: 'endTime',
    component: 'DatePicker',
    componentProps: {
      showTime: true,
      valueFormat: 'YYYY-MM-DD hh:mm:ss',
    },
  },
  {
    label: '店铺描述',
    field: 'description',
    component: 'Input',
  },
  {
    label: '经度',
    field: 'longitude',
    component: 'InputNumber',
  },
  {
    label: '维度',
    field: 'latitude',
    component: 'InputNumber',
  },
  {
    label: 'remark',
    field: 'remark',
    component: 'Input',
  },
  {
    label: '0:失效 1:有效',
    field: 'isDelete',
    component: 'Input',
  },
];
