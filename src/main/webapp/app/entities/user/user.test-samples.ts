import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 20612,
  login: 'D8ABQ0',
};

export const sampleWithPartialData: IUser = {
  id: 11000,
  login: '!*c@s5ZK',
};

export const sampleWithFullData: IUser = {
  id: 7164,
  login: 'aoG7pw@d\\2soE\\MdV-3T\\32Qj2',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
