import dayjs from 'dayjs/esm';

import { IStatement, NewStatement } from './statement.model';

export const sampleWithRequiredData: IStatement = {
  id: 27960,
  dateCreation: dayjs('2024-10-12T12:15'),
  destination: 'heartfelt worriedly countess',
  pathCsv: 'worth replicate',
};

export const sampleWithPartialData: IStatement = {
  id: 14041,
  dateCreation: dayjs('2024-10-12T01:48'),
  destination: 'gallivant',
  pathCsv: 'electrify ha offensively',
};

export const sampleWithFullData: IStatement = {
  id: 12961,
  dateCreation: dayjs('2024-10-12T09:09'),
  destination: 'energetically napkin microchip',
  pathCsv: 'whoever insert coliseum',
};

export const sampleWithNewData: NewStatement = {
  dateCreation: dayjs('2024-10-12T18:13'),
  destination: 'mount exterior difficult',
  pathCsv: 'nor and',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
