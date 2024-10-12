import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicleType, NewVehicleType } from '../vehicle-type.model';

export type PartialUpdateVehicleType = Partial<IVehicleType> & Pick<IVehicleType, 'id'>;

export type EntityResponseType = HttpResponse<IVehicleType>;
export type EntityArrayResponseType = HttpResponse<IVehicleType[]>;

@Injectable({ providedIn: 'root' })
export class VehicleTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicle-types');

  create(vehicleType: NewVehicleType): Observable<EntityResponseType> {
    return this.http.post<IVehicleType>(this.resourceUrl, vehicleType, { observe: 'response' });
  }

  update(vehicleType: IVehicleType): Observable<EntityResponseType> {
    return this.http.put<IVehicleType>(`${this.resourceUrl}/${this.getVehicleTypeIdentifier(vehicleType)}`, vehicleType, {
      observe: 'response',
    });
  }

  partialUpdate(vehicleType: PartialUpdateVehicleType): Observable<EntityResponseType> {
    return this.http.patch<IVehicleType>(`${this.resourceUrl}/${this.getVehicleTypeIdentifier(vehicleType)}`, vehicleType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVehicleType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehicleType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVehicleTypeIdentifier(vehicleType: Pick<IVehicleType, 'id'>): number {
    return vehicleType.id;
  }

  compareVehicleType(o1: Pick<IVehicleType, 'id'> | null, o2: Pick<IVehicleType, 'id'> | null): boolean {
    return o1 && o2 ? this.getVehicleTypeIdentifier(o1) === this.getVehicleTypeIdentifier(o2) : o1 === o2;
  }

  addVehicleTypeToCollectionIfMissing<Type extends Pick<IVehicleType, 'id'>>(
    vehicleTypeCollection: Type[],
    ...vehicleTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vehicleTypes: Type[] = vehicleTypesToCheck.filter(isPresent);
    if (vehicleTypes.length > 0) {
      const vehicleTypeCollectionIdentifiers = vehicleTypeCollection.map(vehicleTypeItem => this.getVehicleTypeIdentifier(vehicleTypeItem));
      const vehicleTypesToAdd = vehicleTypes.filter(vehicleTypeItem => {
        const vehicleTypeIdentifier = this.getVehicleTypeIdentifier(vehicleTypeItem);
        if (vehicleTypeCollectionIdentifiers.includes(vehicleTypeIdentifier)) {
          return false;
        }
        vehicleTypeCollectionIdentifiers.push(vehicleTypeIdentifier);
        return true;
      });
      return [...vehicleTypesToAdd, ...vehicleTypeCollection];
    }
    return vehicleTypeCollection;
  }
}
