import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVehicleType } from '../vehicle-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../vehicle-type.test-samples';

import { VehicleTypeService } from './vehicle-type.service';

const requireRestSample: IVehicleType = {
  ...sampleWithRequiredData,
};

describe('VehicleType Service', () => {
  let service: VehicleTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IVehicleType | IVehicleType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VehicleTypeService);
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

    it('should create a VehicleType', () => {
      const vehicleType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vehicleType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VehicleType', () => {
      const vehicleType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vehicleType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VehicleType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VehicleType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VehicleType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVehicleTypeToCollectionIfMissing', () => {
      it('should add a VehicleType to an empty array', () => {
        const vehicleType: IVehicleType = sampleWithRequiredData;
        expectedResult = service.addVehicleTypeToCollectionIfMissing([], vehicleType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleType);
      });

      it('should not add a VehicleType to an array that contains it', () => {
        const vehicleType: IVehicleType = sampleWithRequiredData;
        const vehicleTypeCollection: IVehicleType[] = [
          {
            ...vehicleType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVehicleTypeToCollectionIfMissing(vehicleTypeCollection, vehicleType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VehicleType to an array that doesn't contain it", () => {
        const vehicleType: IVehicleType = sampleWithRequiredData;
        const vehicleTypeCollection: IVehicleType[] = [sampleWithPartialData];
        expectedResult = service.addVehicleTypeToCollectionIfMissing(vehicleTypeCollection, vehicleType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleType);
      });

      it('should add only unique VehicleType to an array', () => {
        const vehicleTypeArray: IVehicleType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vehicleTypeCollection: IVehicleType[] = [sampleWithRequiredData];
        expectedResult = service.addVehicleTypeToCollectionIfMissing(vehicleTypeCollection, ...vehicleTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vehicleType: IVehicleType = sampleWithRequiredData;
        const vehicleType2: IVehicleType = sampleWithPartialData;
        expectedResult = service.addVehicleTypeToCollectionIfMissing([], vehicleType, vehicleType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleType);
        expect(expectedResult).toContain(vehicleType2);
      });

      it('should accept null and undefined values', () => {
        const vehicleType: IVehicleType = sampleWithRequiredData;
        expectedResult = service.addVehicleTypeToCollectionIfMissing([], null, vehicleType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleType);
      });

      it('should return initial array if no VehicleType is added', () => {
        const vehicleTypeCollection: IVehicleType[] = [sampleWithRequiredData];
        expectedResult = service.addVehicleTypeToCollectionIfMissing(vehicleTypeCollection, undefined, null);
        expect(expectedResult).toEqual(vehicleTypeCollection);
      });
    });

    describe('compareVehicleType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVehicleType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVehicleType(entity1, entity2);
        const compareResult2 = service.compareVehicleType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVehicleType(entity1, entity2);
        const compareResult2 = service.compareVehicleType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVehicleType(entity1, entity2);
        const compareResult2 = service.compareVehicleType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
