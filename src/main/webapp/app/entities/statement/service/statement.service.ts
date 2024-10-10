import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStatement, NewStatement } from '../statement.model';

export type PartialUpdateStatement = Partial<IStatement> & Pick<IStatement, 'id'>;

type RestOf<T extends IStatement | NewStatement> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

export type RestStatement = RestOf<IStatement>;

export type NewRestStatement = RestOf<NewStatement>;

export type PartialUpdateRestStatement = RestOf<PartialUpdateStatement>;

export type EntityResponseType = HttpResponse<IStatement>;
export type EntityArrayResponseType = HttpResponse<IStatement[]>;

@Injectable({ providedIn: 'root' })
export class StatementService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/statements');

  create(statement: NewStatement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(statement);
    return this.http
      .post<RestStatement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(statement: IStatement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(statement);
    return this.http
      .put<RestStatement>(`${this.resourceUrl}/${this.getStatementIdentifier(statement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(statement: PartialUpdateStatement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(statement);
    return this.http
      .patch<RestStatement>(`${this.resourceUrl}/${this.getStatementIdentifier(statement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestStatement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestStatement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStatementIdentifier(statement: Pick<IStatement, 'id'>): number {
    return statement.id;
  }

  compareStatement(o1: Pick<IStatement, 'id'> | null, o2: Pick<IStatement, 'id'> | null): boolean {
    return o1 && o2 ? this.getStatementIdentifier(o1) === this.getStatementIdentifier(o2) : o1 === o2;
  }

  addStatementToCollectionIfMissing<Type extends Pick<IStatement, 'id'>>(
    statementCollection: Type[],
    ...statementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const statements: Type[] = statementsToCheck.filter(isPresent);
    if (statements.length > 0) {
      const statementCollectionIdentifiers = statementCollection.map(statementItem => this.getStatementIdentifier(statementItem));
      const statementsToAdd = statements.filter(statementItem => {
        const statementIdentifier = this.getStatementIdentifier(statementItem);
        if (statementCollectionIdentifiers.includes(statementIdentifier)) {
          return false;
        }
        statementCollectionIdentifiers.push(statementIdentifier);
        return true;
      });
      return [...statementsToAdd, ...statementCollection];
    }
    return statementCollection;
  }

  protected convertDateFromClient<T extends IStatement | NewStatement | PartialUpdateStatement>(statement: T): RestOf<T> {
    return {
      ...statement,
      dateCreation: statement.dateCreation?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restStatement: RestStatement): IStatement {
    return {
      ...restStatement,
      dateCreation: restStatement.dateCreation ? dayjs(restStatement.dateCreation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestStatement>): HttpResponse<IStatement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestStatement[]>): HttpResponse<IStatement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
