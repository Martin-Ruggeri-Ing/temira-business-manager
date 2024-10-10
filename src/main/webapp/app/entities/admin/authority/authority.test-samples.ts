import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '9a96bcd1-4e24-44b6-b1e5-ad8fc806025f',
};

export const sampleWithPartialData: IAuthority = {
  name: '21abfafd-c38d-44c7-a47b-a0f77cf20dc0',
};

export const sampleWithFullData: IAuthority = {
  name: '9c926a2c-afcb-4d97-8873-15eef93e08a6',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
