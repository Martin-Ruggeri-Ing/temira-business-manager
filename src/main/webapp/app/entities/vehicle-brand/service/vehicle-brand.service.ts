import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicleBrand, NewVehicleBrand } from '../vehicle-brand.model';

export type PartialUpdateVehicleBrand = Partial<IVehicleBrand> & Pick<IVehicleBrand, 'id'>;

export type EntityResponseType = HttpResponse<IVehicleBrand>;
export type EntityArrayResponseType = HttpResponse<IVehicleBrand[]>;

@Injectable({ providedIn: 'root' })
export class VehicleBrandService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicle-brands');

  create(vehicleBrand: NewVehicleBrand): Observable<EntityResponseType> {
    return this.http.post<IVehicleBrand>(this.resourceUrl, vehicleBrand, { observe: 'response' });
  }

  update(vehicleBrand: IVehicleBrand): Observable<EntityResponseType> {
    return this.http.put<IVehicleBrand>(`${this.resourceUrl}/${this.getVehicleBrandIdentifier(vehicleBrand)}`, vehicleBrand, {
      observe: 'response',
    });
  }

  partialUpdate(vehicleBrand: PartialUpdateVehicleBrand): Observable<EntityResponseType> {
    return this.http.patch<IVehicleBrand>(`${this.resourceUrl}/${this.getVehicleBrandIdentifier(vehicleBrand)}`, vehicleBrand, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVehicleBrand>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehicleBrand[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVehicleBrandIdentifier(vehicleBrand: Pick<IVehicleBrand, 'id'>): number {
    return vehicleBrand.id;
  }

  compareVehicleBrand(o1: Pick<IVehicleBrand, 'id'> | null, o2: Pick<IVehicleBrand, 'id'> | null): boolean {
    return o1 && o2 ? this.getVehicleBrandIdentifier(o1) === this.getVehicleBrandIdentifier(o2) : o1 === o2;
  }

  addVehicleBrandToCollectionIfMissing<Type extends Pick<IVehicleBrand, 'id'>>(
    vehicleBrandCollection: Type[],
    ...vehicleBrandsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vehicleBrands: Type[] = vehicleBrandsToCheck.filter(isPresent);
    if (vehicleBrands.length > 0) {
      const vehicleBrandCollectionIdentifiers = vehicleBrandCollection.map(vehicleBrandItem =>
        this.getVehicleBrandIdentifier(vehicleBrandItem),
      );
      const vehicleBrandsToAdd = vehicleBrands.filter(vehicleBrandItem => {
        const vehicleBrandIdentifier = this.getVehicleBrandIdentifier(vehicleBrandItem);
        if (vehicleBrandCollectionIdentifiers.includes(vehicleBrandIdentifier)) {
          return false;
        }
        vehicleBrandCollectionIdentifiers.push(vehicleBrandIdentifier);
        return true;
      });
      return [...vehicleBrandsToAdd, ...vehicleBrandCollection];
    }
    return vehicleBrandCollection;
  }
}
