import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IStatement, NewStatement } from '../statement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStatement for edit and NewStatementFormGroupInput for create.
 */
type StatementFormGroupInput = IStatement | PartialWithRequiredKeyOf<NewStatement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IStatement | NewStatement> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

type StatementFormRawValue = FormValueOf<IStatement>;

type NewStatementFormRawValue = FormValueOf<NewStatement>;

type StatementFormDefaults = Pick<NewStatement, 'id' | 'dateCreation'>;

type StatementFormGroupContent = {
  id: FormControl<StatementFormRawValue['id'] | NewStatement['id']>;
  dateCreation: FormControl<StatementFormRawValue['dateCreation']>;
  destination: FormControl<StatementFormRawValue['destination']>;
  pathCsv: FormControl<StatementFormRawValue['pathCsv']>;
  sleepDetector: FormControl<StatementFormRawValue['sleepDetector']>;
  vehicle: FormControl<StatementFormRawValue['vehicle']>;
  driver: FormControl<StatementFormRawValue['driver']>;
  user: FormControl<StatementFormRawValue['user']>;
};

export type StatementFormGroup = FormGroup<StatementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StatementFormService {
  createStatementFormGroup(statement: StatementFormGroupInput = { id: null }): StatementFormGroup {
    const statementRawValue = this.convertStatementToStatementRawValue({
      ...this.getFormDefaults(),
      ...statement,
    });
    return new FormGroup<StatementFormGroupContent>({
      id: new FormControl(
        { value: statementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateCreation: new FormControl(statementRawValue.dateCreation, {
        validators: [Validators.required],
      }),
      destination: new FormControl(statementRawValue.destination, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      pathCsv: new FormControl(statementRawValue.pathCsv, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      sleepDetector: new FormControl(statementRawValue.sleepDetector),
      vehicle: new FormControl(statementRawValue.vehicle),
      driver: new FormControl(statementRawValue.driver),
      user: new FormControl(statementRawValue.user),
    });
  }

  getStatement(form: StatementFormGroup): IStatement | NewStatement {
    return this.convertStatementRawValueToStatement(form.getRawValue() as StatementFormRawValue | NewStatementFormRawValue);
  }

  resetForm(form: StatementFormGroup, statement: StatementFormGroupInput): void {
    const statementRawValue = this.convertStatementToStatementRawValue({ ...this.getFormDefaults(), ...statement });
    form.reset(
      {
        ...statementRawValue,
        id: { value: statementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): StatementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
    };
  }

  private convertStatementRawValueToStatement(rawStatement: StatementFormRawValue | NewStatementFormRawValue): IStatement | NewStatement {
    return {
      ...rawStatement,
      dateCreation: dayjs(rawStatement.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertStatementToStatementRawValue(
    statement: IStatement | (Partial<NewStatement> & StatementFormDefaults),
  ): StatementFormRawValue | PartialWithRequiredKeyOf<NewStatementFormRawValue> {
    return {
      ...statement,
      dateCreation: statement.dateCreation ? statement.dateCreation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
