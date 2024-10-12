import { ISleepDetector, NewSleepDetector } from './sleep-detector.model';

export const sampleWithRequiredData: ISleepDetector = {
  id: 11298,
  name: 'pulverize who',
};

export const sampleWithPartialData: ISleepDetector = {
  id: 15481,
  name: 'once',
};

export const sampleWithFullData: ISleepDetector = {
  id: 25741,
  name: 'celsius',
};

export const sampleWithNewData: NewSleepDetector = {
  name: 'meanwhile geez',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
