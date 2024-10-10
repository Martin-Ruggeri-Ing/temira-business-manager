import dayjs from 'dayjs/esm';

import { IStatement, NewStatement } from './statement.model';

export const sampleWithRequiredData: IStatement = {
  id: 26963,
  dateCreation: dayjs('2024-10-10T02:54'),
  destination: 'rosemary',
  pathCsv: 'brr because',
};

export const sampleWithPartialData: IStatement = {
  id: 15921,
  dateCreation: dayjs('2024-10-09T09:08'),
  destination: 'above brandish',
  pathCsv: 'where yet at',
};

export const sampleWithFullData: IStatement = {
  id: 25334,
  dateCreation: dayjs('2024-10-09T07:19'),
  destination: 'where so',
  pathCsv: 'worriedly',
};

export const sampleWithNewData: NewStatement = {
  dateCreation: dayjs('2024-10-09T12:06'),
  destination: 'lowball experienced',
  pathCsv: 'below whoa video',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
