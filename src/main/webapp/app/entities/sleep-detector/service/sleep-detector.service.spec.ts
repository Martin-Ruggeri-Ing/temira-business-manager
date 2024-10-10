import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISleepDetector } from '../sleep-detector.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../sleep-detector.test-samples';

import { SleepDetectorService } from './sleep-detector.service';

const requireRestSample: ISleepDetector = {
  ...sampleWithRequiredData,
};

describe('SleepDetector Service', () => {
  let service: SleepDetectorService;
  let httpMock: HttpTestingController;
  let expectedResult: ISleepDetector | ISleepDetector[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SleepDetectorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SleepDetector', () => {
      const sleepDetector = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sleepDetector).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SleepDetector', () => {
      const sleepDetector = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sleepDetector).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SleepDetector', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SleepDetector', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SleepDetector', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSleepDetectorToCollectionIfMissing', () => {
      it('should add a SleepDetector to an empty array', () => {
        const sleepDetector: ISleepDetector = sampleWithRequiredData;
        expectedResult = service.addSleepDetectorToCollectionIfMissing([], sleepDetector);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sleepDetector);
      });

      it('should not add a SleepDetector to an array that contains it', () => {
        const sleepDetector: ISleepDetector = sampleWithRequiredData;
        const sleepDetectorCollection: ISleepDetector[] = [
          {
            ...sleepDetector,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSleepDetectorToCollectionIfMissing(sleepDetectorCollection, sleepDetector);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SleepDetector to an array that doesn't contain it", () => {
        const sleepDetector: ISleepDetector = sampleWithRequiredData;
        const sleepDetectorCollection: ISleepDetector[] = [sampleWithPartialData];
        expectedResult = service.addSleepDetectorToCollectionIfMissing(sleepDetectorCollection, sleepDetector);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sleepDetector);
      });

      it('should add only unique SleepDetector to an array', () => {
        const sleepDetectorArray: ISleepDetector[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sleepDetectorCollection: ISleepDetector[] = [sampleWithRequiredData];
        expectedResult = service.addSleepDetectorToCollectionIfMissing(sleepDetectorCollection, ...sleepDetectorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sleepDetector: ISleepDetector = sampleWithRequiredData;
        const sleepDetector2: ISleepDetector = sampleWithPartialData;
        expectedResult = service.addSleepDetectorToCollectionIfMissing([], sleepDetector, sleepDetector2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sleepDetector);
        expect(expectedResult).toContain(sleepDetector2);
      });

      it('should accept null and undefined values', () => {
        const sleepDetector: ISleepDetector = sampleWithRequiredData;
        expectedResult = service.addSleepDetectorToCollectionIfMissing([], null, sleepDetector, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sleepDetector);
      });

      it('should return initial array if no SleepDetector is added', () => {
        const sleepDetectorCollection: ISleepDetector[] = [sampleWithRequiredData];
        expectedResult = service.addSleepDetectorToCollectionIfMissing(sleepDetectorCollection, undefined, null);
        expect(expectedResult).toEqual(sleepDetectorCollection);
      });
    });

    describe('compareSleepDetector', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSleepDetector(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSleepDetector(entity1, entity2);
        const compareResult2 = service.compareSleepDetector(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSleepDetector(entity1, entity2);
        const compareResult2 = service.compareSleepDetector(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSleepDetector(entity1, entity2);
        const compareResult2 = service.compareSleepDetector(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
