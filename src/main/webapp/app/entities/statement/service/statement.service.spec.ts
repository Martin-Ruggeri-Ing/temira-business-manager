import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStatement } from '../statement.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../statement.test-samples';

import { RestStatement, StatementService } from './statement.service';

const requireRestSample: RestStatement = {
  ...sampleWithRequiredData,
  dateCreation: sampleWithRequiredData.dateCreation?.toJSON(),
};

describe('Statement Service', () => {
  let service: StatementService;
  let httpMock: HttpTestingController;
  let expectedResult: IStatement | IStatement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StatementService);
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

    it('should create a Statement', () => {
      const statement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(statement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Statement', () => {
      const statement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(statement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Statement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Statement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Statement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStatementToCollectionIfMissing', () => {
      it('should add a Statement to an empty array', () => {
        const statement: IStatement = sampleWithRequiredData;
        expectedResult = service.addStatementToCollectionIfMissing([], statement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(statement);
      });

      it('should not add a Statement to an array that contains it', () => {
        const statement: IStatement = sampleWithRequiredData;
        const statementCollection: IStatement[] = [
          {
            ...statement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStatementToCollectionIfMissing(statementCollection, statement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Statement to an array that doesn't contain it", () => {
        const statement: IStatement = sampleWithRequiredData;
        const statementCollection: IStatement[] = [sampleWithPartialData];
        expectedResult = service.addStatementToCollectionIfMissing(statementCollection, statement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(statement);
      });

      it('should add only unique Statement to an array', () => {
        const statementArray: IStatement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const statementCollection: IStatement[] = [sampleWithRequiredData];
        expectedResult = service.addStatementToCollectionIfMissing(statementCollection, ...statementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const statement: IStatement = sampleWithRequiredData;
        const statement2: IStatement = sampleWithPartialData;
        expectedResult = service.addStatementToCollectionIfMissing([], statement, statement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(statement);
        expect(expectedResult).toContain(statement2);
      });

      it('should accept null and undefined values', () => {
        const statement: IStatement = sampleWithRequiredData;
        expectedResult = service.addStatementToCollectionIfMissing([], null, statement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(statement);
      });

      it('should return initial array if no Statement is added', () => {
        const statementCollection: IStatement[] = [sampleWithRequiredData];
        expectedResult = service.addStatementToCollectionIfMissing(statementCollection, undefined, null);
        expect(expectedResult).toEqual(statementCollection);
      });
    });

    describe('compareStatement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStatement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStatement(entity1, entity2);
        const compareResult2 = service.compareStatement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStatement(entity1, entity2);
        const compareResult2 = service.compareStatement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStatement(entity1, entity2);
        const compareResult2 = service.compareStatement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
