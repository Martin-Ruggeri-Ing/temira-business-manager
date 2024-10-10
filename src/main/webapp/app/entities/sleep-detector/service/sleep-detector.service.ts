import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISleepDetector, NewSleepDetector } from '../sleep-detector.model';

export type PartialUpdateSleepDetector = Partial<ISleepDetector> & Pick<ISleepDetector, 'id'>;

export type EntityResponseType = HttpResponse<ISleepDetector>;
export type EntityArrayResponseType = HttpResponse<ISleepDetector[]>;

@Injectable({ providedIn: 'root' })
export class SleepDetectorService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sleep-detectors');

  create(sleepDetector: NewSleepDetector): Observable<EntityResponseType> {
    return this.http.post<ISleepDetector>(this.resourceUrl, sleepDetector, { observe: 'response' });
  }

  update(sleepDetector: ISleepDetector): Observable<EntityResponseType> {
    return this.http.put<ISleepDetector>(`${this.resourceUrl}/${this.getSleepDetectorIdentifier(sleepDetector)}`, sleepDetector, {
      observe: 'response',
    });
  }

  partialUpdate(sleepDetector: PartialUpdateSleepDetector): Observable<EntityResponseType> {
    return this.http.patch<ISleepDetector>(`${this.resourceUrl}/${this.getSleepDetectorIdentifier(sleepDetector)}`, sleepDetector, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISleepDetector>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISleepDetector[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSleepDetectorIdentifier(sleepDetector: Pick<ISleepDetector, 'id'>): number {
    return sleepDetector.id;
  }

  compareSleepDetector(o1: Pick<ISleepDetector, 'id'> | null, o2: Pick<ISleepDetector, 'id'> | null): boolean {
    return o1 && o2 ? this.getSleepDetectorIdentifier(o1) === this.getSleepDetectorIdentifier(o2) : o1 === o2;
  }

  addSleepDetectorToCollectionIfMissing<Type extends Pick<ISleepDetector, 'id'>>(
    sleepDetectorCollection: Type[],
    ...sleepDetectorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sleepDetectors: Type[] = sleepDetectorsToCheck.filter(isPresent);
    if (sleepDetectors.length > 0) {
      const sleepDetectorCollectionIdentifiers = sleepDetectorCollection.map(sleepDetectorItem =>
        this.getSleepDetectorIdentifier(sleepDetectorItem),
      );
      const sleepDetectorsToAdd = sleepDetectors.filter(sleepDetectorItem => {
        const sleepDetectorIdentifier = this.getSleepDetectorIdentifier(sleepDetectorItem);
        if (sleepDetectorCollectionIdentifiers.includes(sleepDetectorIdentifier)) {
          return false;
        }
        sleepDetectorCollectionIdentifiers.push(sleepDetectorIdentifier);
        return true;
      });
      return [...sleepDetectorsToAdd, ...sleepDetectorCollection];
    }
    return sleepDetectorCollection;
  }
}
