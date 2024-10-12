import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../statement.test-samples';

import { StatementFormService } from './statement-form.service';

describe('Statement Form Service', () => {
  let service: StatementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StatementFormService);
  });

  describe('Service methods', () => {
    describe('createStatementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStatementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateCreation: expect.any(Object),
            destination: expect.any(Object),
            pathCsv: expect.any(Object),
            sleepDetector: expect.any(Object),
            vehicle: expect.any(Object),
            driver: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IStatement should create a new form with FormGroup', () => {
        const formGroup = service.createStatementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateCreation: expect.any(Object),
            destination: expect.any(Object),
            pathCsv: expect.any(Object),
            sleepDetector: expect.any(Object),
            vehicle: expect.any(Object),
            driver: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getStatement', () => {
      it('should return NewStatement for default Statement initial value', () => {
        const formGroup = service.createStatementFormGroup(sampleWithNewData);

        const statement = service.getStatement(formGroup) as any;

        expect(statement).toMatchObject(sampleWithNewData);
      });

      it('should return NewStatement for empty Statement initial value', () => {
        const formGroup = service.createStatementFormGroup();

        const statement = service.getStatement(formGroup) as any;

        expect(statement).toMatchObject({});
      });

      it('should return IStatement', () => {
        const formGroup = service.createStatementFormGroup(sampleWithRequiredData);

        const statement = service.getStatement(formGroup) as any;

        expect(statement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStatement should not enable id FormControl', () => {
        const formGroup = service.createStatementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStatement should disable id FormControl', () => {
        const formGroup = service.createStatementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
