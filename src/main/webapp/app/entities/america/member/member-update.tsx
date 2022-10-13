import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMember } from 'app/shared/model/america/member.model';
import { getEntity, updateEntity, createEntity, reset } from './member.reducer';

export const MemberUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const memberEntity = useAppSelector(state => state.member.entity);
  const loading = useAppSelector(state => state.member.loading);
  const updating = useAppSelector(state => state.member.updating);
  const updateSuccess = useAppSelector(state => state.member.updateSuccess);
  const handleClose = () => {
    props.history.push('/member' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...memberEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...memberEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="americaApp.americaMember.home.createOrEditLabel" data-cy="MemberCreateUpdateHeading">
            Create or edit a Member
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="member-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="First Name"
                id="member-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  maxLength: { value: 35, message: 'This field cannot be longer than 35 characters.' },
                }}
              />
              <ValidatedField label="Last Name" id="member-lastName" name="lastName" data-cy="lastName" type="text" />
              <ValidatedField label="Birth Date" id="member-birthDate" name="birthDate" data-cy="birthDate" type="date" />
              <ValidatedField label="Gender" id="member-gender" name="gender" data-cy="gender" type="text" />
              <ValidatedField label="Address" id="member-address" name="address" data-cy="address" type="text" />
              <ValidatedField label="City" id="member-city" name="city" data-cy="city" type="text" />
              <ValidatedField label="State" id="member-state" name="state" data-cy="state" type="text" />
              <ValidatedField label="Zip" id="member-zip" name="zip" data-cy="zip" type="text" />
              <ValidatedField label="County" id="member-county" name="county" data-cy="county" type="text" />
              <ValidatedField label="Country" id="member-country" name="country" data-cy="country" type="text" />
              <ValidatedField
                label="Tobacco Use Indicator"
                id="member-tobaccoUseIndicator"
                name="tobaccoUseIndicator"
                data-cy="tobaccoUseIndicator"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Substance Abuse Indicator"
                id="member-substanceAbuseIndicator"
                name="substanceAbuseIndicator"
                data-cy="substanceAbuseIndicator"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Last Update Datetime"
                id="member-lastUpdateDatetime"
                name="lastUpdateDatetime"
                data-cy="lastUpdateDatetime"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/member" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MemberUpdate;
